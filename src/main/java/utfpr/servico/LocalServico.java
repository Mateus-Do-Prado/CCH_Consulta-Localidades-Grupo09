package utfpr.servico;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import utfpr.http.ClienteHttp;
import utfpr.model.Municipio;
import utfpr.model.UF;

public class LocalServico implements LocalidadeService {
    private static final String BASE = "https://servicodados.ibge.gov.br/api/v1/localidades";

    private final ClienteHttp cliente;

    public LocalServico(ClienteHttp cliente) {
        this.cliente = cliente;
    }

    @Override
    public List<UF> listarUFs() {
        try {
            String json = cliente.buscaDados(BASE + "/estados?orderBy=nome");
            JSONArray array = new JSONArray(json);
            List<UF> ufs = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                JSONObject o = array.getJSONObject(i);
                ufs.add(new UF(o.getInt("id"), o.getString("nome"), o.getString("sigla")));
            }
            return ufs;
        } catch (JSONException e) {
            throw new LocalidadeException("Resposta inesperada da API ao carregar as UFs.", e);
        } catch (RuntimeException e) {
            throw new LocalidadeException("Não foi possível acessar a API do IBGE. Verifique a conexão.", e);
        }
    }

    @Override
    public List<Municipio> listarMunicipios(String siglaUf) {
        if (siglaUf == null || siglaUf.isBlank()) {
            throw new IllegalArgumentException("Selecione uma UF.");
        }
        try {
            String json = cliente.buscaDados(
                    BASE + "/estados/" + siglaUf + "/municipios?orderBy=nome");
            JSONArray array = new JSONArray(json);
            List<Municipio> municipios = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                JSONObject o = array.getJSONObject(i);
                municipios.add(new Municipio(
                        o.getInt("id"), o.getString("nome"), siglaUf, extrairRegiao(o)));
            }
            return municipios;
        } catch (JSONException e) {
            throw new LocalidadeException(
                    "Resposta inesperada da API ao carregar os municípios de " + siglaUf + ".", e);
        } catch (RuntimeException e) {
            throw new LocalidadeException(
                    "Não foi possível acessar a API do IBGE. Verifique a conexão.", e);
        }
    }

    private String extrairRegiao(JSONObject municipio) {
        JSONObject ufJson = null;
        JSONObject micro = municipio.optJSONObject("microrregiao");
        if (micro != null) {
            JSONObject meso = micro.optJSONObject("mesorregiao");
            if (meso != null) {
                ufJson = meso.optJSONObject("UF");
            }
        }
        if (ufJson == null) {
            JSONObject imediata = municipio.optJSONObject("regiao-imediata");
            if (imediata != null) {
                JSONObject inter = imediata.optJSONObject("regiao-intermediaria");
                if (inter != null) {
                    ufJson = inter.optJSONObject("UF");
                }
            }
        }
        if (ufJson == null) {
            return "";
        }
        JSONObject regiao = ufJson.optJSONObject("regiao");
        return regiao == null ? "" : regiao.optString("nome", "");
    }
}