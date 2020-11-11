/**
 * 
 */
package com.co.app.auth.authorize;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.co.app.auth.domain.AuthPermission;
import com.co.app.auth.domain.AuthRole;
import com.co.app.auth.domain.AuthUser;
import com.co.app.auth.domain.CustomUserDetails;
import com.co.app.auth.services.role.AuthRoleService;
import com.co.app.auth.services.user.AuthUserService;

/**
 * This class provide the mechanism to handle your custom pre authorize logic.
 * 
 * @author alobaton
 *
 */
public class CustomMethodSecurityExpressionRoot extends SecurityExpressionRoot
		implements MethodSecurityExpressionOperations {

	private static final Log LOGGER = LogFactory.getLog(CustomMethodSecurityExpressionRoot.class);

	private Object filterObject;
	private Object returnObject;

	private AuthUserService userService;
	private AuthRoleService roleService;

	public CustomMethodSecurityExpressionRoot(Authentication authentication, AuthUserService userService,
			AuthRoleService roleService) {
		super(authentication);

		this.userService = userService;
		this.roleService = roleService;
	}

	/**
	 * Validate if the request has access to the requested source.
	 * 
	 * @param scope The method scope. Eg. create:user. See auth_permissions table.
	 * @return True if has permission, false otherwise.
	 */
	@Transactional
	public boolean customHasPermission(@NotNull String scope) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();

		LOGGER.debug(String.format("Requested resource %s", request.getRequestURI()));
		LOGGER.debug(String.format("Principal object %s", this.getAuthentication().getPrincipal()));

		// Get user roles.
		AuthUser user = null;
		if (this.getAuthentication().getPrincipal() instanceof String) {
			user = userService.get((String) this.getAuthentication().getPrincipal());
		} else if (this.getAuthentication().getPrincipal() instanceof CustomUserDetails) {
			CustomUserDetails userDetails = ((CustomUserDetails) this.getAuthentication().getPrincipal());
			user = userService.get(userDetails.getUsername());
		}

		if (user == null) {
			throw new UnauthorizedUserException("user can't be null");
		}

		// Validate against role's permissions.
		for (String roleId : user.getRoles()) {
			AuthRole role = roleService.get(roleId);

			List<String> permissionsIdList = role.getPermissions().stream().map(AuthPermission::getId)
					.collect(Collectors.toList());
			if (permissionsIdList.contains(scope)) {
				return Boolean.TRUE;
			}
		}

		return Boolean.FALSE;
	}

	@Override
	public void setFilterObject(Object filterObject) {
		this.filterObject = filterObject;
	}

	@Override
	public Object getFilterObject() {
		return this.filterObject;
	}

	@Override
	public void setReturnObject(Object returnObject) {
		this.returnObject = returnObject;
	}

	@Override
	public Object getReturnObject() {
		return this.returnObject;
	}

	@Override
	public Object getThis() {
		return null;
	}
}