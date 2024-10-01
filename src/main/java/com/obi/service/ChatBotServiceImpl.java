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
public class ChatBotServiceImpl implements ChatbotService {

    String GEMINI_API_KEY = "AIzaSyAL-1EPzs7oAksCNELykmyF5h-xNHykZZ4";

    private double convertToDouble(Object value) {
        if (value instanceof Integer) {
            return ((Integer) value).doubleValue();
        } else if (value instanceof Double) {
            return (Double) value;
        } else if (value instanceof Long) {
            return ((Long) value).doubleValue();
        } else throw new IllegalArgumentException("unsupported type" + value.getClass().getName());
    }

    public CoinDto makeApiRequest(String currencyName) throws Exception {
        String url = "https://api.coingecko.com/api/v3/coins/" + currencyName;

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<Map> responseEntity = restTemplate.getForEntity(url, Map.class);
        Map<String, Object> responseBody = responseEntity.getBody();

        if (responseBody != null) {
            Map<String, Object> image = (Map<String, Object>) responseBody.get("image");
            Map<String, Object> marketData = (Map<String, Object>) responseBody.get("market_data");
            CoinDto coinDto = new CoinDto();
            coinDto.setId((String) responseBody.get("id"));
            coinDto.setName((String) responseBody.get("name"));
            coinDto.setSymbol((String) responseBody.get("symbol"));
            coinDto.setImage((String) image.get("large"));

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
            coinDto.setCirculatingSupply(convertToDouble((marketData.get("circulating_supply"))));
            coinDto.setTotalSupply(convertToDouble((marketData.get("total_supply"))));

            return coinDto;
        }
        throw new Exception("coin not found");
    }

    @Override
    public ApiResponse getCoinDetails(String prompt) throws Exception {
        FunctionResponse res = getFunctionResponse(prompt);
        CoinDto apiResponse = makeApiRequest(res.getCurrencyName().toLowerCase());


        String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key="
                + GEMINI_API_KEY;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        //create JSON body using method chaining
        String body = new JSONObject()
                .put("contents", new JSONArray()
                        .put(new JSONObject()
                                .put("role", "user")
                                .put("parts", new JSONArray()
                                        .put(new JSONObject()
                                                .put("text", prompt)
                                        )
                                )
                        )
                        .put(new JSONObject()
                                .put("role", "model")
                                .put("parts", new JSONArray()
                                        .put(new JSONObject()
                                                .put("functionCall", new JSONObject()
                                                        .put("name", "getCoinDetails")
                                                        .put("args", new JSONObject()
                                                                .put("currencyName", res.getCurrencyName())
                                                                .put("currencyData", res.getCurrencyData())
                                                        )
                                                )
                                        )
                                )
                        )
                        .put(new JSONObject()
                                .put("role", "function")
                                .put("parts", new JSONArray()
                                        .put(new JSONObject()
                                                .put("functionResponse", new JSONObject()
                                                        .put("name", "getCoinDetails")
                                                        .put("response", new JSONObject()
                                                                .put("name", new JSONObject()
                                                                        .put("name", "getCoinDetails")
                                                                        .put("content", apiResponse)
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
                .put("tools", new JSONArray()
                        .put(new JSONObject()
                                .put("functionDeclarations", new JSONArray()
                                        .put(new JSONObject()
                                                .put("name", "getCoinDetails")
                                                .put("description", "Get crypto currency data from given currency object")
                                                .put("parameters", new JSONObject()
                                                        .put("type", "OBJECT")
                                                        .put("properties", new JSONObject()
                                                                .put("currencyName", new JSONObject()
                                                                        .put("type", "STRING")
                                                                        .put("description",
                                                                                "The currency name, id, symbol."
                                                                        )
                                                                )
                                                                .put("currencyData", new JSONObject()
                                                                        .put("type", "STRING")
                                                                        .put("description",
                                                                                "Currency Data id, name, symbol, image, " +
                                                                                        "current_price, market_cap, market_cap_rank, " +
                                                                                        "fully_diluted_valuation, total_volume, high_24h, " +
                                                                                        "low_24h, price_change_24h, price_change_percentage_24h, " +
                                                                                        "market_cap_change_24h, market_cap_change_percentage_24h, " +
                                                                                        "circulating_supply, total_supply, max_supply, ath, " +
                                                                                        "ath_change_percentage, ath_date, atl, atl_change_percentage, " +
                                                                                        "last_updated."
                                                                        )
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
                ).toString();

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(GEMINI_API_URL, request, String.class);

        String responseBody = response.getBody();
        System.out.println("-------" + responseBody);

        // Parse the JSON string-
        JSONObject root = new JSONObject(responseBody);
        JSONArray candidates = root.getJSONArray("candidates");

        ApiResponse ans = new ApiResponse();
        if (candidates.length() > 0) {
            JSONObject firstCandidate = candidates.getJSONObject(0);
            JSONObject content = firstCandidate.getJSONObject("content");
            JSONArray parts = content.getJSONArray("parts");

            if (parts.length() > 0) {
                String textValue = parts.getJSONObject(0).getString("text");
                System.out.println("Extracted Text: " + textValue);
                ans.setMessage(textValue);
            }
        }
        return ans;
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


        // Parse the JSON string
        JSONObject root = new JSONObject(responseBody);
        JSONArray candidates = root.getJSONArray("candidates");
        FunctionResponse res = new FunctionResponse();

        if (candidates.length() > 0) {
            JSONObject firstCandidate = candidates.getJSONObject(0);
            JSONObject content = firstCandidate.getJSONObject("content");
            JSONArray parts = content.getJSONArray("parts");

            if (parts.length() > 0) {
                JSONObject functionCall = parts.getJSONObject(0).getJSONObject("functionCall");
                String functionName = functionCall.getString("name");
                String currencyName = functionCall.getJSONObject("args").getString("currencyName");
                String currencyData = functionCall.getJSONObject("args").getString("currencyData");


                res.setFunctionName(functionName);
                res.setCurrencyName(currencyName);
                res.setCurrencyData(currencyData);
                // Output the extracted values
                System.out.println("Function Name: " + functionName);
                System.out.println("Currency Name: " + currencyName);
                System.out.println("Currency Data: " + currencyData);
            }
        }


//        System.out.println("responseBody = " + responseBody);
        return res;
    }
}
