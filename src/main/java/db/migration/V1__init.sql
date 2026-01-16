-- Estrutura das tabelas

DROP TABLE IF EXISTS acesso;
CREATE TABLE acesso (
  id BIGINT NOT NULL,
  descricao VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS avaliacao_produto;
CREATE TABLE avaliacao_produto (
  id BIGINT NOT NULL,
  descricao VARCHAR(255) NOT NULL,
  nota INT NOT NULL,
  pessoa_id BIGINT NOT NULL,
  produto_id BIGINT NOT NULL,
  PRIMARY KEY (id),
  KEY avaliacao_produto_fk (produto_id),
  CONSTRAINT avaliacao_produto_fk FOREIGN KEY (produto_id) REFERENCES produto (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Triggers

DELIMITER //

CREATE TRIGGER avaliacao_produto_pessoa_id_insert
BEFORE INSERT ON avaliacao_produto
FOR EACH ROW
BEGIN
    DECLARE existe INT;

    SELECT COUNT(*) INTO existe
      FROM pessoa_fisica
     WHERE id = NEW.pessoa_id;

    IF (existe <= 0) THEN
        SELECT COUNT(*) INTO existe
          FROM pessoa_juridica
         WHERE id = NEW.pessoa_id;

        IF (existe <= 0) THEN
            SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = 'Não foi encontrado o ID e PK da pessoa para realizar a associação do cadastro';
        END IF;
    END IF;
END//

CREATE TRIGGER avaliacao_produto_pessoa_id_update
BEFORE UPDATE ON avaliacao_produto
FOR EACH ROW
BEGIN
    DECLARE existe INT;

    SELECT COUNT(*) INTO existe
      FROM pessoa_fisica
     WHERE id = NEW.pessoa_id;

    IF (existe <= 0) THEN
        SELECT COUNT(*) INTO existe
          FROM pessoa_juridica
         WHERE id = NEW.pessoa_id;

        IF (existe <= 0) THEN
            SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = 'Não foi encontrado o ID e PK da pessoa para realizar a associação do cadastro';
        END IF;
    END IF;
END//

DELIMITER ;

-- Table structure for table `categoria_produto`

DROP TABLE IF EXISTS categoria_produto;
CREATE TABLE categoria_produto (
  id BIGINT NOT NULL,
  nome_desc VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS conta_pagar;
CREATE TABLE conta_pagar (
  id BIGINT NOT NULL,
  descricao VARCHAR(255) NOT NULL,
  dt_pagamento DATE DEFAULT NULL,
  dt_vencimento DATE NOT NULL,
  status ENUM('ABERTA','ALUGUEL','COBRANCA','FUNCIONARIO','QUITADA','RENEGOCIADA','VENCIDA') NOT NULL,
  valor_desconto DECIMAL(38,2) DEFAULT NULL,
  valor_total DECIMAL(38,2) NOT NULL,
  pessoa_id BIGINT NOT NULL,
  pessoa_forn_id BIGINT NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Triggers

DELIMITER //

CREATE TRIGGER conta_pagar_pessoa_id_insert
BEFORE INSERT ON conta_pagar
FOR EACH ROW
BEGIN
    DECLARE existe INT;

    SELECT COUNT(*) INTO existe
      FROM pessoa_fisica
     WHERE id = NEW.pessoa_id;

    IF (existe <= 0) THEN
        SELECT COUNT(*) INTO existe
          FROM pessoa_juridica
         WHERE id = NEW.pessoa_id;

        IF (existe <= 0) THEN
            SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = 'Não foi encontrado o ID e PK da pessoa para realizar a associação do cadastro';
        END IF;
    END IF;
END//

CREATE TRIGGER conta_pagar_pessoa_forn_id_insert
BEFORE INSERT ON conta_pagar
FOR EACH ROW
BEGIN
    DECLARE existe INT;

    SELECT COUNT(*) INTO existe
      FROM pessoa_fisica
     WHERE id = NEW.pessoa_forn_id;

    IF (existe <= 0) THEN
        SELECT COUNT(*) INTO existe
          FROM pessoa_juridica
         WHERE id = NEW.pessoa_forn_id;

        IF (existe <= 0) THEN
            SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = 'Não foi encontrado o ID e PK da pessoa para realizar a associação do cadastro';
        END IF;
    END IF;
END//

CREATE TRIGGER conta_pagar_pessoa_id_update
BEFORE UPDATE ON conta_pagar
FOR EACH ROW
BEGIN
    DECLARE existe INT;

    SELECT COUNT(*) INTO existe
      FROM pessoa_fisica
     WHERE id = NEW.pessoa_id;

    IF (existe <= 0) THEN
        SELECT COUNT(*) INTO existe
          FROM pessoa_juridica
         WHERE id = NEW.pessoa_id;

        IF (existe <= 0) THEN
            SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = 'Não foi encontrado o ID e PK da pessoa para realizar a associação do cadastro';
        END IF;
    END IF;
END//

CREATE TRIGGER conta_pagar_pessoa_forn_id_update
BEFORE UPDATE ON conta_pagar
FOR EACH ROW
BEGIN
    DECLARE existe INT;

    SELECT COUNT(*) INTO existe
      FROM pessoa_fisica
     WHERE id = NEW.pessoa_forn_id;

    IF (existe <= 0) THEN
        SELECT COUNT(*) INTO existe
          FROM pessoa_juridica
         WHERE id = NEW.pessoa_forn_id;

        IF (existe <= 0) THEN
            SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = 'Não foi encontrado o ID e PK da pessoa para realizar a associação do cadastro';
        END IF;
    END IF;
END//

DELIMITER ;

-- Table structure for table `conta_receber`

DROP TABLE IF EXISTS conta_receber;
CREATE TABLE conta_receber (
  id BIGINT NOT NULL,
  descricao VARCHAR(255) NOT NULL,
  dt_pagamento DATE DEFAULT NULL,
  dt_vencimento DATE NOT NULL,
  status ENUM('ABERTA','COBRANCA','QUITADA','VENCIDA') NOT NULL,
  valor_desconto DECIMAL(38,2) DEFAULT NULL,
  valor_total DECIMAL(38,2) NOT NULL,
  pessoa_id BIGINT NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DELIMITER //

CREATE TRIGGER conta_receber_pessoa_id_insert
BEFORE INSERT ON conta_receber
FOR EACH ROW
BEGIN
    DECLARE existe INT;

    SELECT COUNT(*) INTO existe
      FROM pessoa_fisica
     WHERE id = NEW.pessoa_id;

    IF (existe <= 0) THEN
        SELECT COUNT(*) INTO existe
          FROM pessoa_juridica
         WHERE id = NEW.pessoa_id;

        IF (existe <= 0) THEN
            SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = 'Não foi encontrado o ID e PK da pessoa para realizar a associação do cadastro';
        END IF;
    END IF;
END//

CREATE TRIGGER conta_receber_pessoa_id_update
BEFORE UPDATE ON conta_receber
FOR EACH ROW
BEGIN
    DECLARE existe INT;

    SELECT COUNT(*) INTO existe
      FROM pessoa_fisica
     WHERE id = NEW.pessoa_id;

    IF (existe <= 0) THEN
        SELECT COUNT(*) INTO existe
          FROM pessoa_juridica
         WHERE id = NEW.pessoa_id;

        IF (existe <= 0) THEN
            SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = 'Não foi encontrado o ID e PK da pessoa para realizar a associação do cadastro';
        END IF;
    END IF;
END//

DELIMITER ;

-- Estrutura da tabela cup_desc

DROP TABLE IF EXISTS cup_desc;
CREATE TABLE cup_desc (
  id BIGINT NOT NULL,
  valor_porcent_desc DECIMAL(38,2) DEFAULT NULL,
  valor_real_desc DECIMAL(38,2) DEFAULT NULL,
  cod_desc VARCHAR(255) NOT NULL,
  dt_validade_cupom DATE NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Estrutura da tabela endereco

DROP TABLE IF EXISTS endereco;
CREATE TABLE endereco (
  id BIGINT NOT NULL,
  bairro VARCHAR(255) NOT NULL,
  cep VARCHAR(255) NOT NULL,
  cidade VARCHAR(255) NOT NULL,
  complemento VARCHAR(255) DEFAULT NULL,
  numero VARCHAR(255) NOT NULL,
  rua_logradouro VARCHAR(255) NOT NULL,
  tipo_endereco ENUM('COBRANCA','ENTREGA') DEFAULT NULL,
  uf VARCHAR(255) NOT NULL,
  pessoa_id BIGINT NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DELIMITER //

CREATE TRIGGER endereco_pessoa_id_insert
BEFORE INSERT ON endereco
FOR EACH ROW
BEGIN
    DECLARE existe INT;

    SELECT COUNT(*) INTO existe
      FROM pessoa_fisica
     WHERE id = NEW.pessoa_id;

    IF (existe <= 0) THEN
        SELECT COUNT(*) INTO existe
          FROM pessoa_juridica
         WHERE id = NEW.pessoa_id;

        IF (existe <= 0) THEN
            SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = 'Não foi encontrado o ID e PK da pessoa para realizar a associação do cadastro';
        END IF;
    END IF;
END//

CREATE TRIGGER endereco_pessoa_id_update
BEFORE UPDATE ON endereco
FOR EACH ROW
BEGIN

-- Table structure for table `form_pagamento`

DROP TABLE IF EXISTS form_pagamento;
CREATE TABLE form_pagamento (
  id BIGINT NOT NULL,
  descricao VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Estrutura da tabela imagem_produto

DROP TABLE IF EXISTS imagem_produto;
CREATE TABLE imagem_produto (
  id BIGINT NOT NULL,
  imagem_miniatura TEXT NOT NULL,
  imagem_original TEXT NOT NULL,
  produto_id BIGINT NOT NULL,
  PRIMARY KEY (id),
  KEY imagem_produto_produto_fk (produto_id),
  CONSTRAINT imagem_produto_produto_fk FOREIGN KEY (produto_id) REFERENCES produto (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Estrutura da tabela item_venda_loja

DROP TABLE IF EXISTS item_venda_loja;
CREATE TABLE item_venda_loja (
  id BIGINT NOT NULL,
  venda_compra_loja_virtu_id BIGINT NOT NULL,
  quantidade DOUBLE NOT NULL,
  produto_id BIGINT NOT NULL,
  PRIMARY KEY (id),
  KEY venda_compra_loja_virtu_fk (venda_compra_loja_virtu_id),
  KEY produto_fk (produto_id),
  CONSTRAINT produto_fk FOREIGN KEY (produto_id) REFERENCES produto (id),
  CONSTRAINT venda_compra_loja_virtu_fk FOREIGN KEY (venda_compra_loja_virtu_id) REFERENCES vd_cp_loja_virt (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Estrutura da tabela marca_produto

DROP TABLE IF EXISTS marca_produto;
CREATE TABLE marca_produto (
  id BIGINT NOT NULL,
  nome_desc VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Estrutura da tabela nota_fiscal_compra

DROP TABLE IF EXISTS nota_fiscal_compra;
CREATE TABLE nota_fiscal_compra (
  id BIGINT NOT NULL,
  descricao_nota VARCHAR(255) DEFAULT NULL,
  dt_compra DATETIME(6) NOT NULL,
  numero_nota VARCHAR(255) NOT NULL,
  serie_nota VARCHAR(255) NOT NULL,
  valor_desconto DECIMAL(38,2) DEFAULT NULL,
  valor_icms DECIMAL(38,2) NOT NULL,
  valor_total DECIMAL(38,2) NOT NULL,
  conta_pagar_id BIGINT NOT NULL,
  pessoa_id BIGINT NOT NULL,
  PRIMARY KEY (id),
  KEY conta_pagar_fk (conta_pagar_id),
  CONSTRAINT conta_pagar_fk FOREIGN KEY (conta_pagar_id) REFERENCES conta_pagar (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DELIMITER //

CREATE TRIGGER nota_fiscal_compra_pessoa_id_insert
BEFORE INSERT ON nota_fiscal_compra
FOR EACH ROW
BEGIN
    DECLARE existe INT;

    SELECT COUNT(*) INTO existe
      FROM pessoa_fisica
     WHERE id = NEW.pessoa_id;

    IF (existe <= 0) THEN
        SELECT COUNT(*) INTO existe
          FROM pessoa_juridica
         WHERE id = NEW.pessoa_id;

        IF (existe <= 0) THEN
            SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = 'Não foi encontrado o ID e PK da pessoa para realizar a associação do cadastro';
        END IF;
    END IF;
END//

CREATE TRIGGER nota_fiscal_compra_pessoa_id_update
BEFORE UPDATE ON nota_fiscal_compra
FOR EACH ROW
BEGIN
    DECLARE existe INT;

    SELECT COUNT(*) INTO existe
      FROM pessoa_fisica
     WHERE id = NEW.pessoa_id;

    IF (existe <= 0) THEN
        SELECT COUNT(*) INTO existe
          FROM pessoa_juridica
         WHERE id = NEW.pessoa_id;

        IF (existe <= 0) THEN
            SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = 'Não foi encontrado o ID e PK da pessoa para realizar a associação do cadastro';
        END IF;
    END IF;
END//

DELIMITER ;

-- Estrutura da tabela nota_fiscal_venda

DROP TABLE IF EXISTS nota_fiscal_venda;
CREATE TABLE nota_fiscal_venda (
  id BIGINT NOT NULL,
  numero VARCHAR(255) DEFAULT NULL,
  serie VARCHAR(255) DEFAULT NULL,
  tipo VARCHAR(255) DEFAULT NULL,
  pdf TEXT NOT NULL,
  xml TEXT NOT NULL,
  venda_compra_loja_virt_id BIGINT NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY UK3sg7y5xs15vowbpi2mcql08kg (venda_compra_loja_virt_id),
  CONSTRAINT venda_compra_loja_virt_fk FOREIGN KEY (venda_compra_loja_virt_id) REFERENCES vd_cp_loja_virt (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table structure for table `nota_item_produto`

DROP TABLE IF EXISTS nota_item_produto;
CREATE TABLE nota_item_produto (
  id BIGINT NOT NULL,
  nota_fical_compra_id BIGINT NOT NULL,
  quantidade DOUBLE NOT NULL,
  produto_id BIGINT NOT NULL,
  PRIMARY KEY (id),
  KEY nota_fical_compra_fk (nota_fical_compra_id),
  KEY nota_item_produto_produto_fk (produto_id),
  CONSTRAINT nota_fical_compra_fk FOREIGN KEY (nota_fical_compra_id) REFERENCES nota_fiscal_compra (id),
  CONSTRAINT nota_item_produto_produto_fk FOREIGN KEY (produto_id) REFERENCES produto (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Estrutura da tabela pessoa_fisica

DROP TABLE IF EXISTS pessoa_fisica;
CREATE TABLE pessoa_fisica (
  id BIGINT NOT NULL,
  email VARCHAR(255) NOT NULL,
  nome VARCHAR(255) NOT NULL,
  telefone VARCHAR(255) NOT NULL,
  cpf VARCHAR(255) NOT NULL,
  data_nascimento DATE DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Estrutura da tabela pessoa_juridica

DROP TABLE IF EXISTS pessoa_juridica;
CREATE TABLE pessoa_juridica (
  id BIGINT NOT NULL,
  categoria VARCHAR(255) DEFAULT NULL,
  email VARCHAR(255) NOT NULL,
  nome VARCHAR(255) NOT NULL,
  telefone VARCHAR(255) NOT NULL,
  cnpj VARCHAR(255) NOT NULL,
  insc_estadual VARCHAR(255) NOT NULL,
  insc_municipal VARCHAR(255) DEFAULT NULL,
  nome_fantasia VARCHAR(255) NOT NULL,
  razao_social VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Estrutura da tabela produto

DROP TABLE IF EXISTS produto;
CREATE TABLE produto (
  id BIGINT NOT NULL,
  alerta_qtd_estoque BIT(1) DEFAULT NULL,
  altura DOUBLE NOT NULL,
  ativo BIT(1) NOT NULL,
  descricao TEXT NOT NULL,
  largura DOUBLE NOT NULL,
  link_youtube VARCHAR(255) DEFAULT NULL,
  nome VARCHAR(255) NOT NULL,
  peso DOUBLE NOT NULL,
  profundidade DOUBLE NOT NULL,
  qtd_alerta_estoque INT DEFAULT NULL,
  qtd_clique INT DEFAULT NULL,
  qtd_estoque INT NOT NULL,
  tipo_unidade VARCHAR(255) NOT NULL,
  valor_venda DECIMAL(38,2) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Estrutura da tabela seq_acesso

DROP TABLE IF EXISTS seq_acesso;
CREATE TABLE seq_acesso (
  next_val BIGINT DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Estrutura da tabela seq_avaliacao_produto

DROP TABLE IF EXISTS seq_avaliacao_produto;
CREATE TABLE seq_avaliacao_produto (
  next_val BIGINT DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Estrutura da tabela seq_categoria_produto

DROP TABLE IF EXISTS seq_categoria_produto;
CREATE TABLE seq_categoria_produto (
  next_val BIGINT DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Estrutura da tabela seq_conta_pagar

DROP TABLE IF EXISTS seq_conta_pagar;
CREATE TABLE seq_conta_pagar (
  next_val BIGINT DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Estrutura da tabela seq_conta_receber

DROP TABLE IF EXISTS seq_conta_receber;
CREATE TABLE seq_conta_receber (
  next_val BIGINT DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table structure for table `seq_cup_desc`

DROP TABLE IF EXISTS seq_cup_desc;
CREATE TABLE seq_cup_desc (
  next_val BIGINT DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Estrutura da tabela seq_endereco

DROP TABLE IF EXISTS seq_endereco;
CREATE TABLE seq_endereco (
  next_val BIGINT DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Estrutura da tabela seq_form_pagamento

DROP TABLE IF EXISTS seq_form_pagamento;
CREATE TABLE seq_form_pagamento (
  next_val BIGINT DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Estrutura da tabela seq_imagem_produto

DROP TABLE IF EXISTS seq_imagem_produto;
CREATE TABLE seq_imagem_produto (
  next_val BIGINT DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Estrutura da tabela seq_item_venda_loja

DROP TABLE IF EXISTS seq_item_venda_loja;
CREATE TABLE seq_item_venda_loja (
  next_val BIGINT DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Estrutura da tabela seq_marca_produto

DROP TABLE IF EXISTS seq_marca_produto;
CREATE TABLE seq_marca_produto (
  next_val BIGINT DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Estrutura da tabela seq_nota_fiscal_compra

DROP TABLE IF EXISTS seq_nota_fiscal_compra;
CREATE TABLE seq_nota_fiscal_compra (
  next_val BIGINT DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Estrutura da tabela seq_nota_fiscal_venda

DROP TABLE IF EXISTS seq_nota_fiscal_venda;
CREATE TABLE seq_nota_fiscal_venda (
  next_val BIGINT DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Estrutura da tabela seq_nota_item_produto

DROP TABLE IF EXISTS seq_nota_item_produto;
CREATE TABLE seq_nota_item_produto (
  next_val BIGINT DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Estrutura da tabela seq_pessoa

DROP TABLE IF EXISTS seq_pessoa;
CREATE TABLE seq_pessoa (
  next_val BIGINT DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Estrutura da tabela seq_produto

DROP TABLE IF EXISTS seq_produto;
CREATE TABLE seq_produto (
  next_val BIGINT DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Estrutura da tabela seq_status_rastreio

DROP TABLE IF EXISTS seq_status_rastreio;
CREATE TABLE seq_status_rastreio (
  next_val BIGINT DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Estrutura da tabela seq_usuario

DROP TABLE IF EXISTS seq_usuario;
CREATE TABLE seq_usuario (
  next_val BIGINT DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Estrutura da tabela seq_vd_cp_loja_virt

DROP TABLE IF EXISTS seq_vd_cp_loja_virt;
CREATE TABLE seq_vd_cp_loja_virt (
  next_val BIGINT DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Estrutura da tabela status_rastreio

DROP TABLE IF EXISTS status_rastreio;
CREATE TABLE status_rastreio (
  id BIGINT NOT NULL,
  centro_distribuicao VARCHAR(255) DEFAULT NULL,
  cidade VARCHAR(255) DEFAULT NULL,
  estado VARCHAR(255) DEFAULT NULL,
  status VARCHAR(255) DEFAULT NULL,
  venda_compra_loja_virt_id BIGINT NOT NULL,
  PRIMARY KEY (id),
  KEY status_rastreio_venda_compra_loja_virt_fk (venda_compra_loja_virt_id),
  CONSTRAINT status_rastreio_venda_compra_loja_virt_fk FOREIGN KEY (venda_compra_loja_virt_id) REFERENCES vd_cp_loja_virt (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Table structure for table `usuario`

DROP TABLE IF EXISTS usuario;
CREATE TABLE usuario (
  id BIGINT NOT NULL,
  pessoa_id BIGINT NOT NULL,
  data_atual_senha DATE NOT NULL,
  login VARCHAR(255) NOT NULL,
  senha VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DELIMITER //

CREATE TRIGGER usuario_pessoa_id_insert
BEFORE INSERT ON usuario
FOR EACH ROW
BEGIN
    DECLARE existe INT;

    SELECT COUNT(*) INTO existe
      FROM pessoa_fisica
     WHERE id = NEW.pessoa_id;

    IF (existe <= 0) THEN
        SELECT COUNT(*) INTO existe
          FROM pessoa_juridica
         WHERE id = NEW.pessoa_id;

        IF (existe <= 0) THEN
            SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = 'Não foi encontrado o ID e PK da pessoa para realizar a associação do cadastro';
        END IF;
    END IF;
END//

CREATE TRIGGER usuario_pessoa_id_update
BEFORE UPDATE ON usuario
FOR EACH ROW
BEGIN
    DECLARE existe INT;

    SELECT COUNT(*) INTO existe
      FROM pessoa_fisica
     WHERE id = NEW.pessoa_id;

    IF (existe <= 0) THEN
        SELECT COUNT(*) INTO existe
          FROM pessoa_juridica
         WHERE id = NEW.pessoa_id;

        IF (existe <= 0) THEN
            SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = 'Não foi encontrado o ID e PK da pessoa para realizar a associação do cadastro';
        END IF;
    END IF;
END//

DELIMITER ;

-- Estrutura da tabela usuarios_acesso

DROP TABLE IF EXISTS usuarios_acesso;
CREATE TABLE usuarios_acesso (
  usuario_id BIGINT NOT NULL,
  acesso_id BIGINT NOT NULL,
  UNIQUE KEY unique_acesso_user (usuario_id, acesso_id),
  UNIQUE KEY UK8bak9jswon2id2jbunuqlfl9e (acesso_id),
  CONSTRAINT acesso_fk FOREIGN KEY (acesso_id) REFERENCES acesso (id),
  CONSTRAINT usuario_fk FOREIGN KEY (usuario_id) REFERENCES usuario (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Estrutura da tabela vd_cp_loja_virt

DROP TABLE IF EXISTS vd_cp_loja_virt;
CREATE TABLE vd_cp_loja_virt (
  id BIGINT NOT NULL,
  data_entrega DATE NOT NULL,
  data_venda DATE NOT NULL,
  dia_entrega INT NOT NULL,
  valor_desconto DECIMAL(38,2) DEFAULT NULL,
  valor_frete DECIMAL(38,2) NOT NULL,
  valor_total DECIMAL(38,2) NOT NULL,
  cupom_desconto_id BIGINT DEFAULT NULL,
  endereco_cobranca_id BIGINT NOT NULL,
  endereco_entrega_id BIGINT NOT NULL,
  forma_pagamento_id BIGINT NOT NULL,
  nota_fiscal_venda_id BIGINT NOT NULL,
  pessoa_id BIGINT NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY UKhkxjejv08kldx994j4serhrbu (nota_fiscal_venda_id),
  KEY cupom_desconto_fk (cupom_desconto_id),
  KEY endereco_cobranca_fk (endereco_cobranca_id),
  KEY endereco_entrega_fk (endereco_entrega_id),
  KEY forma_pagamento_fk (forma_pagamento_id),
  CONSTRAINT cupom_desconto_fk FOREIGN KEY (cupom_desconto_id) REFERENCES cup_desc (id),
  CONSTRAINT endereco_cobranca_fk FOREIGN KEY (endereco_cobranca_id) REFERENCES endereco (id),
  CONSTRAINT endereco_entrega_fk FOREIGN KEY (endereco_entrega_id) REFERENCES endereco (id),
  CONSTRAINT forma_pagamento_fk FOREIGN KEY (forma_pagamento_id) REFERENCES form_pagamento (id),
  CONSTRAINT nota_fiscal_venda_fk FOREIGN KEY (nota_fiscal_venda_id) REFERENCES nota_fiscal_venda (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DELIMITER //

CREATE TRIGGER vd_cp_loja_virt_pessoa_id_insert
BEFORE INSERT ON vd_cp_loja_virt
FOR EACH ROW
BEGIN
    DECLARE existe INT;

    SELECT COUNT(*) INTO existe
      FROM pessoa_fisica
     WHERE id = NEW.pessoa_id;

    IF (existe <= 0) THEN
        SELECT COUNT(*) INTO existe
          FROM pessoa_juridica
         WHERE id = NEW.pessoa_id;

        IF (existe <= 0) THEN
            SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = 'Não foi encontrado o ID e PK da pessoa para realizar a associação do cadastro';
        END IF;
    END IF;
END//

CREATE TRIGGER vd_cp_loja_virt_pessoa_id_update
BEFORE UPDATE ON vd_cp_loja_virt
FOR EACH ROW
BEGIN
    DECLARE existe INT;

    SELECT COUNT(*) INTO existe
      FROM pessoa_fisica
     WHERE id = NEW.pessoa_id;

    IF (existe <= 0) THEN
        SELECT COUNT(*) INTO existe
          FROM pessoa_juridica
         WHERE id = NEW.pessoa_id;

        IF (existe <= 0) THEN
            SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = 'Não foi encontrado o ID e PK da pessoa para realizar a associação do cadastro';
        END IF;
    END IF;
END//

DELIMITER ;
