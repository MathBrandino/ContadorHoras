package br.com.caelum.contadorhoras.delegate;

import android.content.Context;

/**
 * Created by matheus on 22/01/16.
 */
public interface LancaHorasDelegate {

    void lidaComErro(Exception erro);
    void lidaComRetorno(int codigo);

    Context getContext();

}
