DROP TABLE vgr_user_site_credential if exists;

CREATE TABLE vgr_user_site_credential
(
  uid character varying(20) NOT NULL,
  site_key character varying(50) NOT NULL,
  site_user character varying(256),
  site_password character varying(20),
  CONSTRAINT vaultkey PRIMARY KEY (uid, site_key)
);
