package br.com.caelum.contadorhoras.converter;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import br.com.caelum.contadorhoras.modelo.Tarefa;

/**
 * Created by matheus on 13/11/15.
 */
public class TarefaConverter {

    public String toJson(List<Tarefa> tarefas) {

        Gson gson = new Gson();
        String tarefasGson = gson.toJson(tarefas);
        Writer writer = new StringWriter();
        try {
            JsonWriter jsonWriter = gson.newJsonWriter(writer);
            jsonWriter.beginObject().name("tarefas").jsonValue(tarefasGson).endObject();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return writer.toString();
    }
}
