package com.obi.service;

import com.obi.dto.CoinDto;
import com.obi.response.ApiResponse;
import com.obi.response.FunctionResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class ChatBotServiceImpl implements ChatbotService{

    String GEMINI_API_KEY = "AIzaSyAL-1EPzs7oAksCNELykmyF5h-xNHykZZ4";

    private double convertToDouble(Object value){
        if(value instanceof Integer){
            return ((Integer) value).doubleValue();
        } else if(value instanceof Double){
            return (Double) value;
        } else if(value instanceof Long){
            return ((Long) value).doubleValue();
        }

        else throw new IllegalArgumentException("unsupported type" + value.getClass().getName());
    }

    public CoinDto makeApiRequest (String currencyName) throws Exception {
        String url = "https://api.coingecko.com/api/v3/coins/ethereum";

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<Map> responseEntity = restTemplate.getForEntity(url, Map.class);
        Map<String, Object> responseBody = responseEntity.getBody();

        if(responseBody != null){
            Map<String, Object> image = (Map<String, Object>) responseBody.get("image");
            Map<String, Object> marketData = (Map<String, Object>) responseBody.get("market_data");
            CoinDto coinDto = new CoinDto();
            coinDto.setId((String) responseBody.get("id"));
            coinDto.setName((String) responseBody.get("name"));
            coinDto.setSymbol((String) responseBody.get("symbol"));
            coinDto.setImage((String)image.get("large"));

//            market data
            coinDto.setCurrentPrice(convertToDouble(((Map<String, Object>) marketData.get("current_price")).
                    get("usd")));
            coinDto.setMarketCap(convertToDouble(((Map<String, Object>) marketData.get("market_cap"))
                    .get("usd")));
            coinDto.setMarketCapRank(convertToDouble(((int) marketData.get("market_cap_rank"))));
            coinDto.setTotalVolume(convertToDouble(((Map<String, Object>) marketData.get("total_volume")).
                    get("usd")));
            coinDto.setHigh24h(convertToDouble(((Map<String, Object>) marketData.get("high_24h")).get("usd")));
            coinDto.setLow24h(convertToDouble(((Map<String, Object>) marketData.get("low_24h")).get("usd")));
            coinDto.setPriceChange24h(convertToDouble((marketData.get("price_change_24h"))));
            coinDto.setPriceChangePercentage24h(convertToDouble((
                     marketData.get("price_change_percentage_24h"))));
            coinDto.setMarketCapChange24h(convertToDouble((
                     marketData.get("market_cap_change_24h"))));
            coinDto.setMarketCapChangePercentage24h(convertToDouble((
                     marketData.get("market_cap_change_percentage_24h"))));
            coinDto.setCirculatingSupply(convertToDouble(( marketData.get("circulating_supply"))));
            coinDto.setTotalSupply(convertToDouble((marketData.get("total_supply"))));

            return coinDto;
        }
        throw new Exception("coin not found");
    }
    @Override
    public ApiResponse getCoinDetails(String prompt) throws Exception {
        CoinDto coinDto = makeApiRequest(prompt);
        getFunctionResponse(prompt);
        System.out.println("coinDto = " + coinDto);
        return null;
    }


//simple chat
    @Override
    public String simpleChat(String prompt) {
        String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key="
                + GEMINI_API_KEY;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestBody = new JSONObject()
                .put("contents", new JSONArray()
                        .put(new JSONObject()
                                .put("parts", new JSONArray()
                                        .put(new JSONObject()
                                                .put("text", prompt))))
                ).toString();
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(GEMINI_API_URL, requestEntity, String.class);
        return response.getBody();
    }

    public FunctionResponse getFunctionResponse(String prompt) {
        String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key="
                + GEMINI_API_KEY;

        JSONObject requestBodyJson = new JSONObject()
                .put("contents", new JSONArray()
                        .put(new JSONObject()
                                .put("parts", new JSONArray()
                                        .put(new JSONObject()
                                                .put("text", prompt))))
                )
                .put("tools", new JSONArray()
                        .put(new JSONObject()
                                .put("functionDeclarations", new JSONArray()
                                        .put(new JSONObject()
                                                .put("name", "getCoinDetails")
                                                .put("description", "Get the coin details from given currency object")
                                                .put("parameters", new JSONObject()
                                                        .put("type", "OBJECT")
                                                        .put("properties", new JSONObject()
                                                                .put("currencyName", new JSONObject()
                                                                        .put("type", "STRING")
                                                                        .put(
                                                                                "description",
                                                                                "The currency name, " +
                                                                                        "id, symbol."
                                                                        )

                                                                )
                                                                .put("currencyData", new JSONObject()
                                                                        .put("type", "STRING")
                                                                        .put("description",
                                                                                "Currency Data id" +
                                                                                "name, symbol, image, " +
                                                                                "current_price, market_cap, " +
                                                                                "market_cap_rank, " +
                                                                                "fully_diluted_valuation, " +
                                                                                "total_volume, high_24h, " +
                                                                                "low_24h, price_change_24h, " +
                                                                                "price_change_percentage_24h, " +
                                                                                "market_cap_change_24h, " +
                                                                                "market_cap_change_percentage_24h, " +
                                                                                "circulating_supply, " +
                                                                                "total_supply, max_supply, " +
                                                                                "ath, ath_change_percentage, " +
                                                                                "ath_date, atl, atl_change_percentage, " +
                                                                                        "last_updated.")
                                                                )
                                                        )
                                                        .put("required", new JSONArray()
                                                                .put("currencyName")
                                                                .put("currencyData")
                                                        )

                                                )
                                        )
                                )
                        )
                );

        //create http headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        //create the http entity with headers and request body
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBodyJson.toString(), headers);


        //make the post request
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(GEMINI_API_URL, requestEntity, String.class);
        String responseBody = response.getBody();

//        ReadContext ctx = JsonPath.parse(responseBody);
//        //extract specific values
//        String currencyName = ctx.read(".candidates[0].content.parts[0].functionCall.args.currencyName");
//        String currencyData = ctx.read(".candidates[0].content.parts[0].functionCall.args.currencyData");
//        String name = ctx.read(".candidates[0].content.parts[0].functionCall.name");
//
//
//        //print the extracted values
//        FunctionResponse res = new FunctionResponse();
//        res.setCurrencyName("bitcoin");
//        res.setFunctionName("getCoinDetails");
//        res.setCurrencyData("data");

        System.out.println("responseBody = " + responseBody);
        return null;
    }
}
