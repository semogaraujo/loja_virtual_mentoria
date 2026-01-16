-- 1) Remover UNIQUE criado pelo @OneToOne antigo
ALTER TABLE nota_fiscal_venda
DROP INDEX UK3sg7y5xs15vowbpi2mcql08kg;

-- 2) Criar índice NORMAL
CREATE INDEX idx_nfv_venda_id
ON nota_fiscal_venda (venda_compra_loja_virt_id);

-- 3) Criar FK correta (N:1 / ManyToOne)
ALTER TABLE nota_fiscal_venda
ADD CONSTRAINT venda_compra_loja_virt_fk
FOREIGN KEY (venda_compra_loja_virt_id)
REFERENCES vd_cp_loja_virt(id);

