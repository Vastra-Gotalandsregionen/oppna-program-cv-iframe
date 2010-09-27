--
-- Copyright 2010 Västra Götalandsregionen
--
--   This library is free software; you can redistribute it and/or modify
--   it under the terms of version 2.1 of the GNU Lesser General Public
--   License as published by the Free Software Foundation.
--
--   This library is distributed in the hope that it will be useful,
--   but WITHOUT ANY WARRANTY; without even the implied warranty of
--   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
--   GNU Lesser General Public License for more details.
--
--   You should have received a copy of the GNU Lesser General Public
--   License along with this library; if not, write to the
--   Free Software Foundation, Inc., 59 Temple Place, Suite 330,
--   Boston, MA 02111-1307  USA
--
--

DROP TABLE vgr_user_site_credential if exists;

CREATE TABLE vgr_user_site_credential
(
  id bigint NOT NULL,
  site_key character varying(50) NOT NULL,
  site_password character varying(256),
  site_user character varying(256),
  uid character varying(50) NOT NULL,
  CONSTRAINT vgr_user_site_credential_pkey PRIMARY KEY (id),
  CONSTRAINT vgr_user_site_credential_uid_key UNIQUE (uid, site_key)
);
