package ni.org.ics.zpo.v2.appmovil.domain;

import java.util.Date;

/**
 * @author ics
 */
public class ZpoV2IndCuidadoFamilia extends BaseMetaData {

    private String recordId;
    private String eventName;
    private Date fechaHoyFci;
    private String cuantosLibrosFci;
    private String cuantasRevistasFui;
    private String materialesJugarMonth;
    private String materialesJugarFci;
    private String variedadJugarFci;
    private String nombreEncuestadorFci;

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Date getFechaHoyFci() {
        return fechaHoyFci;
    }

    public void setFechaHoyFci(Date fechaHoyFci) {
        this.fechaHoyFci = fechaHoyFci;
    }

    public String getCuantosLibrosFci() {
        return cuantosLibrosFci;
    }

    public void setCuantosLibrosFci(String cuantosLibrosFci) {
        this.cuantosLibrosFci = cuantosLibrosFci;
    }

    public String getCuantasRevistasFui() {
        return cuantasRevistasFui;
    }

    public void setCuantasRevistasFui(String cuantasRevistasFui) {
        this.cuantasRevistasFui = cuantasRevistasFui;
    }

    public String getMaterialesJugarMonth() {
        return materialesJugarMonth;
    }

    public void setMaterialesJugarMonth(String materialesJugarMonth) {
        this.materialesJugarMonth = materialesJugarMonth;
    }

    public String getMaterialesJugarFci() {
        return materialesJugarFci;
    }

    public void setMaterialesJugarFci(String materialesJugarFci) {
        this.materialesJugarFci = materialesJugarFci;
    }

    public String getVariedadJugarFci() {
        return variedadJugarFci;
    }

    public void setVariedadJugarFci(String variedadJugarFci) {
        this.variedadJugarFci = variedadJugarFci;
    }

    public String getNombreEncuestadorFci() {
        return nombreEncuestadorFci;
    }

    public void setNombreEncuestadorFci(String nombreEncuestadorFci) {
        this.nombreEncuestadorFci = nombreEncuestadorFci;
    }
}
