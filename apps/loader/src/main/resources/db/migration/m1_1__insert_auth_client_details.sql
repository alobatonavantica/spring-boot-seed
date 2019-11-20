INSERT INTO `auth_client_details` (client_id, resource_ids, secret_required, client_secret, scoped, scope, authorized_grant_types, registered_redirect_uri, authorities, access_token_validity_seconds, refresh_token_validity_seconds, auto_approve, additional_information) 
VALUES ('api_client', JSON_ARRAY('resource_id'), true, '$2a$10$Nh913vrYBSxp2Rf3uq8hjOAAlsIbon8erZE0.vTSh/RmJb7xlmVpm', true, JSON_ARRAY('write', 'read'), JSON_ARRAY('authorization_code', 'client_credentials', 'implicit', 'password', 'refresh_token'), NULL, NULL, 3600, 7200, true, NULL);