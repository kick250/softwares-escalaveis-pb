insert
    into users (name, username, password, roles)
    values (
            'Default ERP USER',
            'default@erp.com.br',
            '$2a$12$Q5xLN1wYfrSRRVAGEwtpMOYcEacINmTNEkpb84DvHFcFiMtj7O4IW',
            '["ERP_USER"]'
);

insert
into users (name, username, password, roles)
values (
           'Default PORTAL USER',
           'default@portal.com.br',
           '$2a$12$Q5xLN1wYfrSRRVAGEwtpMOYcEacINmTNEkpb84DvHFcFiMtj7O4IW',
           '["PORTAL_USER"]'
);

insert
into users (name, username, password, roles)
values (
           'Default PORTAL ADMIN',
           'defaul.admin@portal.com.br',
           '$2a$12$Q5xLN1wYfrSRRVAGEwtpMOYcEacINmTNEkpb84DvHFcFiMtj7O4IW',
           '["PORTAL_USER", "ADMIN_PERMISSION"]'
);
