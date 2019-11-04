
CREATE TABLE wp_posts (
  ID int NOT NULL,
  post_author int NOT NULL,
  post_date datetime,
  post_date_gmt datetime ,
  post_content varchar(255),
  post_title varchar(255),
  post_excerpt varchar(255),
  post_status varchar(255),
  comment_status varchar(255),
  ping_status varchar(255),
  post_password varchar(255),
  post_name varchar(200) ,
  to_ping varchar(255),
  pinged varchar(255),
  post_modified datetime,
  post_modified_gmt datetime,
  post_content_filtered varchar(255),
  post_parent varchar(255),
  guid varchar(255),
  menu_order int,
  post_type varchar(255),
  post_mime_type varchar(100),
  comment_count int,
  PRIMARY KEY (ID)
);

CREATE TABLE wp_postmeta (
  meta_id int NOT NULL,
  post_id int NOT NULL,
  meta_key varchar(255),
  meta_value longtext,
  PRIMARY KEY (meta_id)
);

CREATE TABLE wp_term_relationships (
  object_id int NOT NULL ,
  term_taxonomy_id int NOT NULL,
  term_order int NOT NULL,
  PRIMARY KEY (object_id, term_taxonomy_id)
);

CREATE TABLE wp_term_taxonomy (
  term_taxonomy_id int NOT NULL,
  term_id int NOT NULL,
  taxonomy varchar(32) NOT NULL,
  description varchar(255) NOT NULL,
  parent int NOT NULL,
  count int NOT NULL,
  PRIMARY KEY (term_taxonomy_id)
);

CREATE TABLE wp_terms (
  term_id int NOT NULL,
  name varchar(200) NOT NULL,
  slug varchar(200) NOT NULL,
  term_group int NOT NULL,
  PRIMARY KEY (term_id)
);

-- notícias
INSERT INTO wp_posts(ID,post_author,post_date,post_date_gmt,post_content,post_title,post_excerpt,post_status,comment_status,ping_status,post_password,post_name,
to_ping,pinged,post_modified,post_modified_gmt,post_content_filtered,post_parent,guid,menu_order,post_type,post_mime_type,comment_count)
	VALUES (1, 1, PARSEDATETIME('16/05/2019 10:00:36', 'dd/MM/yyyy HH:mm:ss'), PARSEDATETIME('16/05/2019 10:00:36', 'dd/MM/yyyy HH:mm:ss'), 
'<p>Conteudo teste agencia 1</p>','Titulo teste 1','Teste 1','publish','open','closed','','Teste 1','','',	PARSEDATETIME('16/05/2019 10:00:36', 'dd/MM/yyyy HH:mm:ss'), 
PARSEDATETIME('16/05/2019 10:00:36', 'dd/MM/yyyy HH:mm:ss'),'',	'0','/teste1',0,'radioagencia','',0);

INSERT INTO wp_posts(ID,post_author,post_date,post_date_gmt,post_content,post_title,post_excerpt,post_status,comment_status,ping_status,post_password,post_name,
to_ping,pinged,post_modified,post_modified_gmt,post_content_filtered,post_parent,guid,menu_order,post_type,post_mime_type,comment_count)
	VALUES (2, 1, PARSEDATETIME('16/05/2019 10:00:36', 'dd/MM/yyyy HH:mm:ss'), PARSEDATETIME('16/05/2019 10:00:36', 'dd/MM/yyyy HH:mm:ss'), 
'<p>Conteudo teste agencia 2</p>','Titulo teste 2','Teste 2','publish','open','closed','','Teste 2','','',	PARSEDATETIME('16/05/2019 10:00:36', 'dd/MM/yyyy HH:mm:ss'), 
PARSEDATETIME('16/05/2019 10:00:36', 'dd/MM/yyyy HH:mm:ss'),'',	'0','/teste2',0,'radioagencia','',0);

INSERT INTO wp_posts(ID,post_author,post_date,post_date_gmt,post_content,post_title,post_excerpt,post_status,comment_status,ping_status,post_password,post_name,
to_ping,pinged,post_modified,post_modified_gmt,post_content_filtered,post_parent,guid,menu_order,post_type,post_mime_type,comment_count)
	VALUES (3, 1, PARSEDATETIME('17/05/2019 10:00:36', 'dd/MM/yyyy HH:mm:ss'), PARSEDATETIME('16/05/2019 10:00:36', 'dd/MM/yyyy HH:mm:ss'), 
'<p>Conteudo teste agencia 3</p>','Titulo teste 3','Teste 3','publish','open','closed','','Teste 3','','',	PARSEDATETIME('16/05/2019 10:00:36', 'dd/MM/yyyy HH:mm:ss'), 
PARSEDATETIME('16/05/2019 10:00:36', 'dd/MM/yyyy HH:mm:ss'),'',	'0','/teste3',0,'radioagencia','',0);

INSERT INTO wp_posts(ID,post_author,post_date,post_date_gmt,post_content,post_title,post_excerpt,post_status,comment_status,ping_status,post_password,post_name,
to_ping,pinged,post_modified,post_modified_gmt,post_content_filtered,post_parent,guid,menu_order,post_type,post_mime_type,comment_count)
	VALUES (4, 1, PARSEDATETIME('18/05/2019 10:00:36', 'dd/MM/yyyy HH:mm:ss'), PARSEDATETIME('16/05/2019 10:00:36', 'dd/MM/yyyy HH:mm:ss'), 
'<p>Conteudo teste agencia 4</p>','Titulo teste 4','Teste 4','publish','open','closed','','Teste 4','','',	PARSEDATETIME('16/05/2019 10:00:36', 'dd/MM/yyyy HH:mm:ss'), 
PARSEDATETIME('16/05/2019 10:00:36', 'dd/MM/yyyy HH:mm:ss'),'',	'0','/teste4',0,'radioagencia','',0);

INSERT INTO wp_posts(ID,post_author,post_date,post_date_gmt,post_content,post_title,post_excerpt,post_status,comment_status,ping_status,post_password,post_name,
to_ping,pinged,post_modified,post_modified_gmt,post_content_filtered,post_parent,guid,menu_order,post_type,post_mime_type,comment_count)
	VALUES (5, 1, PARSEDATETIME('18/05/2019 10:00:36', 'dd/MM/yyyy HH:mm:ss'), PARSEDATETIME('16/05/2019 10:00:36', 'dd/MM/yyyy HH:mm:ss'), 
'<p>Conteudo programa de radio 1</p>','Titulo programa radio 1','Programa radio 1','publish','open','closed','','Teste 4','','',	PARSEDATETIME('16/05/2019 10:00:36', 'dd/MM/yyyy HH:mm:ss'), 
PARSEDATETIME('16/05/2019 10:00:36', 'dd/MM/yyyy HH:mm:ss'),'',	'0','/teste4',0,'radioagencia','',0);

INSERT INTO wp_posts(ID,post_author,post_date,post_date_gmt,post_content,post_title,post_excerpt,post_status,comment_status,ping_status,post_password,post_name,
to_ping,pinged,post_modified,post_modified_gmt,post_content_filtered,post_parent,guid,menu_order,post_type,post_mime_type,comment_count)
	VALUES (6, 1, PARSEDATETIME('18/05/2019 10:00:36', 'dd/MM/yyyy HH:mm:ss'), PARSEDATETIME('16/05/2019 10:00:36', 'dd/MM/yyyy HH:mm:ss'), 
'<p>Conteudo programa de radio 2</p>','Titulo programa radio 2','Programa radio 2','publish','open','closed','','Teste 4','','',	PARSEDATETIME('16/05/2019 10:00:36', 'dd/MM/yyyy HH:mm:ss'), 
PARSEDATETIME('16/05/2019 10:00:36', 'dd/MM/yyyy HH:mm:ss'),'',	'0','/teste4',0,'radioagencia','',0);

INSERT INTO wp_posts(ID,post_author,post_date,post_date_gmt,post_content,post_title,post_excerpt,post_status,comment_status,ping_status,post_password,post_name,
to_ping,pinged,post_modified,post_modified_gmt,post_content_filtered,post_parent,guid,menu_order,post_type,post_mime_type,comment_count)
	VALUES (7, 1, PARSEDATETIME('18/05/2019 10:00:36', 'dd/MM/yyyy HH:mm:ss'), PARSEDATETIME('16/05/2019 10:00:36', 'dd/MM/yyyy HH:mm:ss'), 
'<p>Conteudo programa de radio 3</p>','Titulo programa radio 3','Programa radio 3','publish','open','closed','','Teste 4','','',	PARSEDATETIME('16/05/2019 10:00:36', 'dd/MM/yyyy HH:mm:ss'), 
PARSEDATETIME('16/05/2019 10:00:36', 'dd/MM/yyyy HH:mm:ss'),'',	'0','/teste4',0,'radioagencia','',0);

 -- Post Meta
INSERT INTO wp_postmeta(meta_id, post_id, meta_key, meta_value) VALUES (1, 1, 'cd_rodape', 'Materia 1 Rodape');
INSERT INTO wp_postmeta(meta_id, post_id, meta_key, meta_value) VALUES (2, 1, 'cd_deputados', '73653');
INSERT INTO wp_postmeta(meta_id, post_id, meta_key, meta_value) VALUES (3, 1, 'cd_relacionadas', '5');
INSERT INTO wp_postmeta(meta_id, post_id, meta_key, meta_value) VALUES (4, 1, 'cd_relacionadas', '6');

INSERT INTO wp_postmeta(meta_id, post_id, meta_key, meta_value) VALUES (5, 2, 'cd_rodape', 'Materia 2 Rodape');
INSERT INTO wp_postmeta(meta_id, post_id, meta_key, meta_value) VALUES (6, 2, 'cd_deputados', '74047');
INSERT INTO wp_postmeta(meta_id, post_id, meta_key, meta_value) VALUES (13, 2, 'cd_deputados', '141436');
INSERT INTO wp_postmeta(meta_id, post_id, meta_key, meta_value) VALUES (11, 2, 'cd_relacionadas', '6');

INSERT INTO wp_postmeta(meta_id, post_id, meta_key, meta_value) VALUES (7, 3, 'cd_rodape', 'Materia 3 Rodape');
INSERT INTO wp_postmeta(meta_id, post_id, meta_key, meta_value) VALUES (8, 3, 'cd_deputados', '73938');
INSERT INTO wp_postmeta(meta_id, post_id, meta_key, meta_value) VALUES (12, 3, 'cd_relacionadas', '7');

INSERT INTO wp_postmeta(meta_id, post_id, meta_key, meta_value) VALUES (9, 4, 'cd_rodape', 'Materia 4 Rodape');
INSERT INTO wp_postmeta(meta_id, post_id, meta_key, meta_value) VALUES (10, 4, 'cd_deputados', '141436');

-- wp_term_relationships
INSERT INTO wp_term_relationships (object_id, term_taxonomy_id, term_order) VALUES (1, 1, 0);
INSERT INTO wp_term_relationships (object_id, term_taxonomy_id, term_order) VALUES (1, 2, 0);
INSERT INTO wp_term_relationships (object_id, term_taxonomy_id, term_order) VALUES (2, 2, 0);
INSERT INTO wp_term_relationships (object_id, term_taxonomy_id, term_order) VALUES (3, 3, 0);
INSERT INTO wp_term_relationships (object_id, term_taxonomy_id, term_order) VALUES (4, 1, 0);
INSERT INTO wp_term_relationships (object_id, term_taxonomy_id, term_order) VALUES (4, 3, 0);

INSERT INTO wp_term_relationships (object_id, term_taxonomy_id, term_order) VALUES (1, 4, 0);
INSERT INTO wp_term_relationships (object_id, term_taxonomy_id, term_order) VALUES (2, 5, 0);
INSERT INTO wp_term_relationships (object_id, term_taxonomy_id, term_order) VALUES (3, 4, 0);
INSERT INTO wp_term_relationships (object_id, term_taxonomy_id, term_order) VALUES (3, 5, 0);
INSERT INTO wp_term_relationships (object_id, term_taxonomy_id, term_order) VALUES (4, 6, 0);

-- wp_term_taxonomy
INSERT INTO wp_term_taxonomy (term_taxonomy_id, term_id, taxonomy, description, parent, count) VALUES (1, 1, 'post_tag', '', 0, 0);
INSERT INTO wp_term_taxonomy (term_taxonomy_id, term_id, taxonomy, description, parent, count) VALUES (2, 2, 'post_tag', '', 0, 0);
INSERT INTO wp_term_taxonomy (term_taxonomy_id, term_id, taxonomy, description, parent, count) VALUES (3, 3, 'post_tag', '', 0, 0);
INSERT INTO wp_term_taxonomy (term_taxonomy_id, term_id, taxonomy, description, parent, count) VALUES (4, 4, 'tema', '', 0, 0);
INSERT INTO wp_term_taxonomy (term_taxonomy_id, term_id, taxonomy, description, parent, count) VALUES (5, 5, 'tema', '', 0, 0);
INSERT INTO wp_term_taxonomy (term_taxonomy_id, term_id, taxonomy, description, parent, count) VALUES (6, 6, 'tema', '', 0, 0);

-- wp_terms
INSERT INTO wp_terms (term_id, name, slug, term_group) VALUES (1, 'Meio ambiente', 'meio-ambiente', 0);
INSERT INTO wp_terms (term_id, name, slug, term_group) VALUES (2, 'Saiba mais', 'saiba-mais', 0);
INSERT INTO wp_terms (term_id, name, slug, term_group) VALUES (3, 'quebra de decoro', 'quebra-de-decoro', 0);
-- Retrancas
INSERT INTO wp_terms (term_id, name, slug, term_group) VALUES (4, 'Economia', 'economia', 0);
INSERT INTO wp_terms (term_id, name, slug, term_group) VALUES (5, 'Cidades', 'cidades', 0);
INSERT INTO wp_terms (term_id, name, slug, term_group) VALUES (6, 'Trabalho', 'Trabalho', 0);


