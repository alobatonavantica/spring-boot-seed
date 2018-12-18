/**
 * 
 */
package com.innova4j.api.settings.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.innova4j.api.commons.controllers.BaseController;
import com.innova4j.api.settings.dto.SettingsDto;
import com.innova4j.api.settings.services.SettingsService;

/**
 * @author innova4j-team
 *
 */
@RestController
@RequestMapping("/settings")
public class SettingsController implements BaseController<SettingsDto> {

	@Autowired
	private SettingsService service;

	@Override
	public SettingsDto create(@Valid @RequestBody SettingsDto dto) {
		return service.create(dto);
	}

	@Override
	public SettingsDto get(@PathVariable String id) {
		return service.get(id);
	}

	@Override
	public List<SettingsDto> getAll(@Valid @RequestBody SettingsDto dto) {
		return service.getAll(dto);
	}

	@Override
	public SettingsDto update(@PathVariable String id, @Valid @RequestBody SettingsDto dto) {
		dto.setId(id);

		return service.update(dto);
	}

	@Override
	public void delete(@PathVariable String id) {
		service.delete(id);
	}

}
