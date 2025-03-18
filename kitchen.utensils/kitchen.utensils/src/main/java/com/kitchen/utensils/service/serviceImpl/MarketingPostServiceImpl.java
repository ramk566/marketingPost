package com.kitchen.utensils.service.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kitchen.utensils.request.MarketingPost;
import com.kitchen.utensils.request.PromptRequest;
import com.kitchen.utensils.request.PostRequest;
import com.kitchen.utensils.request.RequestDto;
import com.kitchen.utensils.exception.AiApiException;
import com.kitchen.utensils.message.ResponseMessage;
import com.kitchen.utensils.response.ApiResponse;
import com.kitchen.utensils.service.MarketingPostService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.*;

@Service
public class MarketingPostServiceImpl implements MarketingPostService {

    private static final Logger logger = LoggerFactory.getLogger(MarketingPostServiceImpl.class);

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String RUNWARE_AI_URL = "https://api.runware.ai/v1";
    private static final String RUNWARE_API_KEY = "SyTVIqzaW3Luqt04IbRKt9RGswMXOJfp";


    @Override
    public ApiResponse<PromptRequest> generateMarketingPostsPrompt(RequestDto requestDto) {
        try {
            logger.info("Generating marketing post prompts for request: {}", requestDto);

            String systemMessage= " Generate 10 Prompts in JSON array format like:\n" +
                    "[\n" +
                    "{\"No\": 1, \"content\": \"...\"},\n" +
                    "{\"No\": 2, \"content\": \"...\"}\n" +
                    "]\n" +
                    "Return only a valid JSON array, no extra text, no JSON.stringify. Prompt starts with generate/create keyword. Each prompt is at least 3 lines.";

            String response = callOpenAiApi(systemMessage, requestDto.toString());

            List<PromptRequest> prompts = parsePostPromptResponse(response);
            logger.info("Successfully generated {} marketing prompts.", prompts.size());

            return new ApiResponse<>(ResponseMessage.SUCCESS, ResponseMessage.PROMPT_SUCCESS, prompts);
        }catch (AiApiException e) {
            logger.error("Error generating prompts: {}", e.getMessage());
            return new ApiResponse<>(ResponseMessage.ERROR, ResponseMessage.ERROR, null);
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage(), e);
            return new ApiResponse<>(ResponseMessage.ERROR, "An unexpected error occurred: " + e.getMessage(), null);
        }
    }

    @Override
    public ApiResponse<MarketingPost> generateMarketingPost(PostRequest postRequest) {
        try {
            logger.info("Generating marketing posts for request: {}", postRequest);

            String systemMessage= " Create 10 Marketing post in JSON array format like:\n" +
                    "[\n" +
                    "{\"No\": 1, \"content\": \"...\"},\n" +
                    "{\"No\": 2, \"content\": \"...\"}\n" +
                    "]\n" +
                    "Return only a valid JSON array, no extra text, no JSON.stringify.Each Post should be proper emoji and 5 Lines sentences.Our Responses are not 5 lines.Need Response each post should have 5 lines. Company details are MerxTeam is a Swedish company recognized as one of the leading suppliers of restaurant, kitchen, and promotional products in the Nordic countries. Headquartered in Gothenburg, Sweden, the company has built a strong reputation for providing high-quality, professional-grade kitchen and restaurant equipment under its own brands, primarily Exxent, Xantia, and BBM (Basic By Merx). Established in 1973, MerxTeam operates as Merx Team AB, a private entity with a focus on wholesale trade, particularly in the hospitality and retail sectors. Company Overview: Name: Merx Team AB (commonly referred to as MerxTeam). Founded: 1973. Headquarters: Göteborg, Västra Götaland, Sweden. Industry: Wholesale of restaurant and kitchen products, furniture, and promotional items. Revenue: Approximately $15.08 million USD (as reported by Dun & Bradstreet for a recent fiscal year, ending April). Employees: Around 80 employees across its operations, with 20 based at the Gothenburg location. Leadership: Per Magnus Spitale Börjesson serves as the Managing Director and Deputy Member of the Board of Directors. Operations and Reach: MerxTeam operates primarily in the Nordic region but extends its distribution across Europe through a network of retailers and specialized trade partners. The company maintains its head office in Gothenburg, housed in its own premises, and manages distribution from three storage facilities. Additionally, MerxTeam has established international presence with offices in Norway, Finland, Poland, Russia, and a purchasing office in China staffed by local personnel to oversee sourcing and quality control. This global footprint supports its supply chain, enabling the company to offer a wide range of products tailored to professional users in the hospitality industry. Brands and Product Offerings: MerxTeam is known for its diverse portfolio of brands, each catering to specific needs within the restaurant, kitchen, and promotional product markets: 1. Exxent: A premium brand focused on high-quality restaurant and kitchen products, designed with care for professional use. This includes items for cooking, serving, and dining. 2. Xantia: Another key brand offering restaurant and kitchen equipment, often selected for its functionality and durability, complementing Exxent’s range. 3. BBM (Basic By Merx): A line of carefully selected basic products for restaurants and commercial kitchens, maintaining the same restaurant-quality standards as the other brands but positioned as a more affordable option. The product assortment is one of the widest in the market, covering professional cooking equipment, serving and dining ware, outdoor furniture for hospitality settings, promotional products for businesses, and consumer-friendly kitchen items supplied to department stores and specialty shops. All products are optimized for professional use, adhering to restaurant-quality standards, and are distributed to specialist trade, retail, profile dealers, and trading customers. Business Model and Services: MerxTeam operates as a wholesaler, importing and exporting goods with an emphasis on hospitality and kitchen solutions. Its business model includes wholesale distribution—supplying products through retailers across Europe, rather than direct-to-consumer sales; logistics solutions—managing distribution from multiple storage facilities to ensure efficient delivery; and custom sourcing—leveraging its China-based purchasing office to source products tailored to market demands. The company also emphasizes seasonal readiness, such as preparing for high-demand periods like the Christmas season (noted as a peak revenue period for the hospitality industry) and outdoor dining seasons, offering specialized assortments accordingly. Workforce and Culture: With approximately 80 employees, MerxTeam fosters a dynamic work environment. The company actively recruits for roles such as accounting economists, highlighting a culture of entrepreneurship, responsibility, and collaboration. Its LinkedIn presence (with 389 followers as of the latest update) reflects an engaged professional community and ongoing hiring efforts. Financial and Corporate Insights: Revenue: Reported at $21.3 million in 2024 by RocketReach, though Dun & Bradstreet lists $15.08 million, indicating some variation in estimates depending on the fiscal period or reporting method. Corporate Structure: Part of a corporate family with 30 affiliated companies, though specific details about these affiliates are not widely publicized. Industry Codes: Classified under NAICS codes related to manufacturing and wholesale (e.g., 311611 for food-related wholesale) and SIC codes like 201 (meat products) and 20 (food and kindred products), though its primary focus is broader wholesale trade. Online Presence and Customer Engagement: Website: www.merxteam.com provides detailed information about its brands, products, and contact options. The site uses cookies for analytics and user experience enhancement. Social Media: Active on platforms like LinkedIn and Facebook (1,816 likes on its official page), where it shares updates about products, seasonal offerings, and industry partnerships. Customer Base: Serves a mix of professional clients (restaurants, commercial kitchens) and retail partners (department stores, kitchen shops), with a strong emphasis on quality and functionality. Additional Notes: MerxTeam’s commitment to quality and breadth of offerings has positioned it as a trusted partner in the Nordic hospitality sector. The company does not appear to engage in direct consumer sales but focuses on B2B relationships through its trade network. There’s no indication of involvement in unrelated industries (e.g., transportation, as with Merx Global), confirming its specialization in kitchen and restaurant products\n";

            String response = callOpenAiApi(systemMessage, postRequest.toString());
            List<MarketingPost> posts=parsePostResponse(response);

            for(MarketingPost marketingPost:posts){

                String imageUrl = generateImage(marketingPost.getContent());
                marketingPost.setImageUrl(imageUrl);
            }
            logger.info("Successfully generated {} marketing posts.", posts.size());

            return new ApiResponse<>(ResponseMessage.SUCCESS, ResponseMessage.MARKETING_POST_SUCCESS, posts);
        } catch (AiApiException e) {
            logger.error("Error generating marketing posts: {}", e.getMessage());
            return new ApiResponse<>(ResponseMessage.ERROR, ResponseMessage.ERROR, null);
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage(), e);
            return new ApiResponse<>(ResponseMessage.ERROR, "An unexpected error occurred: " + e.getMessage(), null);
        }
    }

    private String callOpenAiApi(String systemMessage, String userMessageContent) throws AiApiException {
        logger.debug("Calling OpenAI API with system message: {}", systemMessage);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        JSONObject requestBody = new JSONObject();

        requestBody.put("model", "gpt-4o-mini");

        JSONArray messages = new JSONArray();

        JSONObject systemMsg = new JSONObject();
        systemMsg.put("role", "system");
        systemMsg.put("content", systemMessage);
        messages.put(systemMsg);

        JSONObject userMsg = new JSONObject();
        userMsg.put("role", "user");
        userMsg.put("content", userMessageContent);
        messages.put(userMsg);

        requestBody.put("messages", messages);

        HttpEntity<String> request = new HttpEntity<>(requestBody.toString(), headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, String.class);
            logger.info("OpenAI API response received successfully.");
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            logger.error("OpenAI API request failed with status code {}: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new AiApiException(e.getStatusCode().value(), e.getResponseBodyAsString());
        } catch (Exception e) {
            logger.error("Unexpected error occurred while calling OpenAI API: {}", e.getMessage(), e);
            throw new AiApiException(500, "An unexpected error occurred: " + e.getMessage());
        }
    }

    private List<MarketingPost> parsePostResponse(String responseBody) throws AiApiException {
        try {
            logger.debug("Parsing marketing post response.");

            JsonNode jsonResponse = objectMapper.readTree(responseBody);
            List<MarketingPost> marketingPosts = new ArrayList<>();

            JsonNode choices = jsonResponse.get("choices");
            if (choices != null && choices.isArray() && !choices.isEmpty()) {
                String content = choices.get(0).get("message").get("content").asText();
                JsonNode postArray = objectMapper.readTree(content);
                for (JsonNode postNode : postArray) {
                    int no = postNode.get("No").asInt();
                    String postContent = postNode.get("content").asText();
                    String imageUrl = postNode.has("imageUrl") ? postNode.get("imageUrl").asText() : "";
                    marketingPosts.add(new MarketingPost(no, postContent,imageUrl));
                }
            }
            logger.info("Parsed {} marketing posts.", marketingPosts.size());
            return marketingPosts;
        } catch (Exception e) {
            logger.error("Failed to parse marketing post response: {}", e.getMessage(), e);
            throw new AiApiException(500, "Failed to parse response: " + e.getMessage());
        }
    }

    private List<PromptRequest> parsePostPromptResponse(String responseBody) throws AiApiException {
        try {
            logger.debug("Parsing marketing post prompt response.");

            JsonNode jsonResponse = objectMapper.readTree(responseBody);
            List<PromptRequest> marketingPosts = new ArrayList<>();

            JsonNode choices = jsonResponse.get("choices");
            if (choices != null && choices.isArray() && !choices.isEmpty()) {
                String content = choices.get(0).get("message").get("content").asText();
                JsonNode postArray = objectMapper.readTree(content);
                for (JsonNode postNode : postArray) {
                    int no = postNode.get("No").asInt();
                    String postContent = postNode.get("content").asText();
                    marketingPosts.add(new PromptRequest(no, postContent));
                }
            }
            logger.info("Parsed {} marketing posts prompt.", marketingPosts.size());

            System.out.println("MarketingPost::"+marketingPosts);
            return marketingPosts;
        } catch (Exception e) {
            logger.error("Failed to parse marketing post response: {}", e.getMessage(), e);
            throw new AiApiException(500, "Failed to parse response: " + e.getMessage());
        }
    }



    private String generateImage(String prompt) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            List<Map<String, Object>> requestBody = new ArrayList<>();
            Map<String, Object> auth = new HashMap<>();
            auth.put("taskType", "authentication");
            auth.put("apiKey", RUNWARE_API_KEY);

            Map<String, Object> imageTask = new HashMap<>();
            imageTask.put("taskType", "imageInference");
            imageTask.put("taskUUID", UUID.randomUUID().toString());
            imageTask.put("positivePrompt", prompt);
            imageTask.put("width", 512);
            imageTask.put("height", 512);
            imageTask.put("model", "civitai:102438@133677");
            imageTask.put("numberResults", 1);

            requestBody.add(auth);
            requestBody.add(imageTask);

            HttpEntity<List<Map<String, Object>>> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.exchange(RUNWARE_AI_URL, HttpMethod.POST, request, String.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {

                JsonNode rootNode = objectMapper.readTree(response.getBody());

                JsonNode dataNode = rootNode.path("data");

                if (dataNode.isArray() && !dataNode.isEmpty()) {
                    return dataNode.get(0).path("imageURL").asText();
                } else {
                    logger.warn("No imageURL found in Runware AI response.");
                }
            }
        } catch (Exception e) {
            logger.error("Failed to generate image from Runware AI", e);
        }
        return null;
    }
}