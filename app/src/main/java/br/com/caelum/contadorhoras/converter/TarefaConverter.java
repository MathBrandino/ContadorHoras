package br.com.caelum.contadorhoras.converter;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONStringer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.caelum.contadorhoras.modelo.Login;
import br.com.caelum.contadorhoras.modelo.Tarefa;

/**
 * Created by matheus on 13/11/15.
 */
public class TarefaConverter {

    public static final String LOGIN = "login";
    public static final String SENHA = "senha";
    public static final String PROJETO = "projeto";
    public static final String INICIO = "inicio";
    public static final String FIM = "fim";
    public static final String TEMPO_ALMOCO = "tempoAlmoco";
    public static final String DURACAO = "duracao";
    public static final String COMENTARIOS = "comentarios";
    public static final String DATA = "data";
    public static final String USUARIO = "usuario";
    public static final String OBJETO = "br.com.caelum.caelumweb2.modelo.pessoas.UsuarioComHoras";
    public static final String ID = "id";
    public static final String HORAS = "horas";
    public static final String LISTA_HORAS_EXECUTADAS = "br.com.caelum.caelumweb2.modelo.pessoas.ListaDeHorasExecutadas";
    public static final String HORA_EXECUTADA = "br.com.caelum.caelumweb2.modelo.consultoria.HoraExecutada";
    public static final String TIME = "time";
    public static final String TIMEZONE = "timezone";

    public String toJson(List<Tarefa> tarefas, Login login) {

        JSONStringer json = new JSONStringer();

        try {

            json.object().key(OBJETO);
            json.object().key(USUARIO);
            json.object()
                    .key(LOGIN).value(login.getLogin())
                    .key(SENHA).value(login.getSenha());
            json.endObject();

            json.key(HORAS).array().object();
            JSONStringer jsonStringer = json.key(HORA_EXECUTADA).array();
            for (Tarefa tarefa : tarefas) {


                JSONStringer jsonTarefa = jsonStringer.object();


                String inicio = geraInicio(tarefa);
                jsonTarefa.key(INICIO).value(inicio);

                String fim = geraFim(tarefa);
                jsonTarefa.key(FIM).value(fim);

                jsonTarefa.key(TEMPO_ALMOCO).value("00:00");

                long duracao = geraDuracao(tarefa);
                jsonTarefa.key(DURACAO).value(duracao);

                jsonTarefa.key(COMENTARIOS).value(tarefa.getDescricao());


                Calendar dataCalendar = getCalendar(tarefa);

                jsonTarefa.key(DATA).object()
                        .key(TIME).value(dataCalendar.getTimeInMillis())
                        .key(TIMEZONE).value(dataCalendar.getTimeZone())
                        .endObject();

                jsonTarefa.key(PROJETO).object().key(ID).value(tarefa.getIdCategoria()).endObject();

                jsonTarefa.endObject();

            }

            jsonStringer.endArray().endObject();

            json.endArray().endObject();

            json.endObject().endObject();


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return json.toString();
    }

    private long geraDuracao(Tarefa tarefa) {

        int horaInicial = tarefa.getHoraInicial();
        int minutoInicial = tarefa.getMinutoInicial();
        int horaFinal = tarefa.getHoraFinal();
        int minutoFinal = tarefa.getMinutoFinal();
        String tempoInicial = tarefa.getDataDia() + " " + horaInicial + ":" + minutoInicial;
        String tempoFinal = tarefa.getDataDia() + " " + horaFinal + ":" + minutoFinal;

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        Date dateFinal = null;
        Date dateInicial = null;
        try {
            dateFinal = dateFormat.parse(tempoFinal);
            dateInicial = dateFormat.parse(tempoInicial);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Long contador = dateFinal.getTime() - dateInicial.getTime();


        long minutos = (contador / 1000) / 60;


        return minutos;
    }

    private String geraFim(Tarefa tarefa) {


        String minutoFinal = String.valueOf(tarefa.getMinutoFinal());
        String horaFinal = String.valueOf(tarefa.getHoraFinal());

        if (tarefa.getMinutoFinal() < 10) {
            minutoFinal = "0" + tarefa.getMinutoFinal();
        }

        if (tarefa.getHoraFinal() < 10) {
            horaFinal = "0" + tarefa.getHoraFinal();
        }

        String fim = horaFinal + ":" + minutoFinal;

        return fim;


    }

    private String geraInicio(Tarefa tarefa) {


        String minutoInicial = String.valueOf(tarefa.getMinutoInicial());
        String horaInicial = String.valueOf(tarefa.getHoraInicial());

        if (tarefa.getMinutoInicial() < 10) {
            minutoInicial = "0" + tarefa.getMinutoInicial();
        }

        if (tarefa.getHoraInicial() < 10) {
            horaInicial = "0" + tarefa.getHoraInicial();
        }

        String inicio = horaInicial + ":" + minutoInicial;

        return inicio;

    }

    @NonNull
    private Calendar getCalendar(Tarefa tarefa) throws ParseException {
        Calendar dataCalendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date dataFormatada = format.parse(tarefa.getDataDia());
        dataCalendar.setTime(dataFormatada);
        return dataCalendar;
    }


}
