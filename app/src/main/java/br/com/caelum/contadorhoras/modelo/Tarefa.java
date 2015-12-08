package br.com.caelum.contadorhoras.modelo;

import java.io.Serializable;

/**
 * Created by matheus on 10/11/15.
 */
public class Tarefa implements Serializable {

    private Long id;
    private Long idCategoria;
    private String dataDia;
    private String desc;
    private int horaInicial;
    private int horaFinal;
    private int minutoInicial;
    private int minutoFinal;

    public String getDataDia() {
        return dataDia;
    }

    public void setDataDia(String dataDia) {
        this.dataDia = dataDia;
    }

    public int getMinutoInicial() {
        return minutoInicial;
    }

    public void setMinutoInicial(int minutoInicial) {
        this.minutoInicial = minutoInicial;
    }

    public int getMinutoFinal() {
        return minutoFinal;
    }

    public void setMinutoFinal(int minutoFinal) {
        this.minutoFinal = minutoFinal;
    }

    public int getHoraFinal() {
        return horaFinal;
    }

    public void setHoraFinal(int horaFinal) {
        this.horaFinal = horaFinal;
    }

    public int getHoraInicial() {
        return horaInicial;
    }

    public void setHoraInicial(int horaInicial) {
        this.horaInicial = horaInicial;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Long idCategoria) {
        this.idCategoria = idCategoria;
    }
}
