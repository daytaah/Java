//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package br.com.constance.cadastro.parceiro;

import br.com.sankhya.extensions.eventoprogramavel.EventoProgramavelJava;
import br.com.sankhya.jape.event.PersistenceEvent;
import br.com.sankhya.jape.event.TransactionContext;
import br.com.sankhya.jape.vo.DynamicVO;
import br.com.sankhya.jape.wrapper.JapeFactory;
import br.com.sankhya.jape.wrapper.JapeWrapper;
import br.com.sankhya.jape.wrapper.fluid.FluidCreateVO;
import com.sankhya.util.TimeUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class RegistraLogMultimarcasEvento implements EventoProgramavelJava {
    private String statusParceiro;
    private BigDecimal motivoStatus;
    private BigDecimal codigoUsuario;
    private Timestamp dataAlteracao;
    private BigDecimal codigoParceiro;
    private JapeWrapper ad_tgflogmmDAO = JapeFactory.dao("AD_TGFLOGMM");

    public RegistraLogMultimarcasEvento() {
    }

    public void beforeInsert(PersistenceEvent event) throws Exception {
    }

    public void beforeUpdate(PersistenceEvent event) throws Exception {
    }

    public void beforeDelete(PersistenceEvent event) throws Exception {
    }

    public void afterInsert(PersistenceEvent event) throws Exception {
    }

    public void afterUpdate(PersistenceEvent event) throws Exception {
        DynamicVO dadosAlterados = (DynamicVO) event.getVo();
        boolean parceiroMultimarcas = dadosAlterados.asBigDecimal("CODTIPPARC").compareTo(new BigDecimal(10200000)) == 0;
        if (parceiroMultimarcas) {
            boolean statusModificado = event.getModifingFields().isModifing("AD_STATUS");
            boolean motivoModificado = event.getModifingFields().isModifing("AD_MOTIVO");
            if (statusModificado && motivoModificado) {
                this.statusParceiro = dadosAlterados.asString("AD_STATUS");
                this.motivoStatus = dadosAlterados.asBigDecimal("AD_MOTIVO");
                this.codigoUsuario = dadosAlterados.asBigDecimal("CODUSU");
                this.dataAlteracao = TimeUtils.getNow();
                this.codigoParceiro = dadosAlterados.asBigDecimal("CODPARC");
                this.gravaLog();
            }
        }
    }

    public void afterDelete(PersistenceEvent event) throws Exception {
    }

    public void beforeCommit(TransactionContext tranCtx) throws Exception {
    }

    private void gravaLog() throws Exception {
        FluidCreateVO fluidCreateVO = this.ad_tgflogmmDAO.create();
        fluidCreateVO.set("STATUS", statusParceiro);
        fluidCreateVO.set("CODUSU", codigoUsuario);
        fluidCreateVO.set("MOTIVO", motivoStatus);
        fluidCreateVO.set("CODPARC", codigoParceiro);
        fluidCreateVO.set("DTALTER", dataAlteracao);
        fluidCreateVO.save();
    }
}



