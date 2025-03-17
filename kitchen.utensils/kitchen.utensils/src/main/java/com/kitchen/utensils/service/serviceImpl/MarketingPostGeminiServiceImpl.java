package com.kitchen.utensils.service.serviceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kitchen.utensils.request.MarketingPost;
import com.kitchen.utensils.message.ResponseMessage;
import com.kitchen.utensils.request.MarketingPostRequest;
import com.kitchen.utensils.response.ApiResponse;
import com.kitchen.utensils.service.MarketingPostGeminiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Service
public class MarketingPostGeminiServiceImpl implements MarketingPostGeminiService {

    private static final Logger logger = LoggerFactory.getLogger(MarketingPostGeminiServiceImpl.class);
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String API_KEY = "AIzaSyADDv36cfbNwvbHTSODWJFIa69eJxuz6fo";
    private static final String URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + API_KEY;

    @Override
    public ApiResponse<MarketingPost> generateContent(MarketingPostRequest prompt) {
        logger.info("Generating marketing post content for request: {}", prompt);

        Map<String, Object> requestBody = new HashMap<>();
        List<Map<String, Object>> contents = new ArrayList<>();
        Map<String, Object> content = new HashMap<>();
        List<Map<String, String>> parts = new ArrayList<>();
        Map<String, String> part = new HashMap<>();

        part.put("text", prompt + " Generate 10 Marketing Posts in JSON array format like:\n" +
                "[{\n" +
                "\"No\": 1,\n" +
                "\"content\": \"...\"\n" +
                "},\n" +
                "{\n" +
                "\"No\": 2,\n" +
                "\"content\": \"...\"\n" +
                "}]\n" +
                " Return only a valid JSON array, no extra text, no JSON.stringify, proper Emoji, and at least 4 lines per post and " +
                "and Company details are MerxTeam is a Swedish company recognized as one of the leading suppliers of restaurant, kitchen, and promotional products in the Nordic countries. Headquartered in Gothenburg, Sweden, the company has built a strong reputation for providing high-quality, professional-grade kitchen and restaurant equipment under its own brands, primarily Exxent, Xantia, and BBM (Basic By Merx). Established in 1973, MerxTeam operates as Merx Team AB, a private entity with a focus on wholesale trade, particularly in the hospitality and retail sectors. Company Overview: Name: Merx Team AB (commonly referred to as MerxTeam). Founded: 1973. Headquarters: Göteborg, Västra Götaland, Sweden. Industry: Wholesale of restaurant and kitchen products, furniture, and promotional items. Revenue: Approximately $15.08 million USD (as reported by Dun & Bradstreet for a recent fiscal year, ending April). Employees: Around 80 employees across its operations, with 20 based at the Gothenburg location. Leadership: Per Magnus Spitale Börjesson serves as the Managing Director and Deputy Member of the Board of Directors. Operations and Reach: MerxTeam operates primarily in the Nordic region but extends its distribution across Europe through a network of retailers and specialized trade partners. The company maintains its head office in Gothenburg, housed in its own premises, and manages distribution from three storage facilities. Additionally, MerxTeam has established international presence with offices in Norway, Finland, Poland, Russia, and a purchasing office in China staffed by local personnel to oversee sourcing and quality control. This global footprint supports its supply chain, enabling the company to offer a wide range of products tailored to professional users in the hospitality industry. Brands and Product Offerings: MerxTeam is known for its diverse portfolio of brands, each catering to specific needs within the restaurant, kitchen, and promotional product markets: 1. Exxent: A premium brand focused on high-quality restaurant and kitchen products, designed with care for professional use. This includes items for cooking, serving, and dining. 2. Xantia: Another key brand offering restaurant and kitchen equipment, often selected for its functionality and durability, complementing Exxent’s range. 3. BBM (Basic By Merx): A line of carefully selected basic products for restaurants and commercial kitchens, maintaining the same restaurant-quality standards as the other brands but positioned as a more affordable option. The product assortment is one of the widest in the market, covering professional cooking equipment, serving and dining ware, outdoor furniture for hospitality settings, promotional products for businesses, and consumer-friendly kitchen items supplied to department stores and specialty shops. All products are optimized for professional use, adhering to restaurant-quality standards, and are distributed to specialist trade, retail, profile dealers, and trading customers. Business Model and Services: MerxTeam operates as a wholesaler, importing and exporting goods with an emphasis on hospitality and kitchen solutions. Its business model includes wholesale distribution—supplying products through retailers across Europe, rather than direct-to-consumer sales; logistics solutions—managing distribution from multiple storage facilities to ensure efficient delivery; and custom sourcing—leveraging its China-based purchasing office to source products tailored to market demands. The company also emphasizes seasonal readiness, such as preparing for high-demand periods like the Christmas season (noted as a peak revenue period for the hospitality industry) and outdoor dining seasons, offering specialized assortments accordingly. Workforce and Culture: With approximately 80 employees, MerxTeam fosters a dynamic work environment. The company actively recruits for roles such as accounting economists, highlighting a culture of entrepreneurship, responsibility, and collaboration. Its LinkedIn presence (with 389 followers as of the latest update) reflects an engaged professional community and ongoing hiring efforts. Financial and Corporate Insights: Revenue: Reported at $21.3 million in 2024 by RocketReach, though Dun & Bradstreet lists $15.08 million, indicating some variation in estimates depending on the fiscal period or reporting method. Corporate Structure: Part of a corporate family with 30 affiliated companies, though specific details about these affiliates are not widely publicized. Industry Codes: Classified under NAICS codes related to manufacturing and wholesale (e.g., 311611 for food-related wholesale) and SIC codes like 201 (meat products) and 20 (food and kindred products), though its primary focus is broader wholesale trade. Online Presence and Customer Engagement: Website: www.merxteam.com provides detailed information about its brands, products, and contact options. The site uses cookies for analytics and user experience enhancement. Social Media: Active on platforms like LinkedIn and Facebook (1,816 likes on its official page), where it shares updates about products, seasonal offerings, and industry partnerships. Customer Base: Serves a mix of professional clients (restaurants, commercial kitchens) and retail partners (department stores, kitchen shops), with a strong emphasis on quality and functionality. Additional Notes: MerxTeam’s commitment to quality and breadth of offerings has positioned it as a trusted partner in the Nordic hospitality sector. The company does not appear to engage in direct consumer sales but focuses on B2B relationships through its trade network. There’s no indication of involvement in unrelated industries (e.g., transportation, as with Merx Global), confirming its specialization in kitchen and restaurant products.\";\n");
        parts.add(part);
        content.put("parts", parts);
        contents.add(content);
        requestBody.put("contents", contents);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            logger.info("Sending request to AI API...");
            ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.POST, request, String.class);
            logger.info("Received response from AI API with status: {}", response.getStatusCode());

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                List<MarketingPost> posts = parseJsonArrayResponse(response.getBody());
                if (!posts.isEmpty()) {
                    logger.info("Successfully generated {} marketing posts.", posts.size());
                    return new ApiResponse<>(ResponseMessage.SUCCESS, ResponseMessage.MARKETING_POST_SUCCESS, posts);
                } else {
                    logger.warn("Failed to extract marketing posts from response.");
                    return new ApiResponse<>(ResponseMessage.ERROR, ResponseMessage.MARKETING_POST_EXTRACTION_ERROR, Collections.emptyList());
                }
            } else {
                logger.error("AI API response error: {}", response.getStatusCode());
                return new ApiResponse<>(ResponseMessage.ERROR, ResponseMessage.MARKETING_POST_GENERATION_ERROR, Collections.emptyList());
            }
        } catch (Exception e) {
            logger.error("Error while communicating with AI API", e);
            return new ApiResponse<>(ResponseMessage.ERROR, ResponseMessage.MARKETING_POST_GENERATION_ERROR, Collections.emptyList());
        }
    }

    private List<MarketingPost> parseJsonArrayResponse(String responseBody) {
        try {
            logger.debug("Parsing AI API response...");
            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode candidates = rootNode.path("candidates");

            if (candidates.isArray() && !candidates.isEmpty()) {
                JsonNode firstCandidate = candidates.get(0);
                JsonNode contentNode = firstCandidate.path("content");

                if (contentNode.has("parts") && contentNode.get("parts").isArray()) {
                    JsonNode partsArray = contentNode.get("parts");

                    if (!partsArray.isEmpty()) {
                        String responseText = partsArray.get(0).path("text").asText();
                        return extractJsonArray(responseText);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Failed to parse AI API response", e);
        }
        return Collections.emptyList();
    }

    private List<MarketingPost> extractJsonArray(String text) throws JsonProcessingException {
        text = text.trim();

        int startIndex = text.indexOf("[");
        int endIndex = text.lastIndexOf("]");

        if (startIndex == -1 || endIndex == -1 || startIndex > endIndex) {
            logger.warn("Response does not contain a valid JSON array.");
            return Collections.emptyList();
        }
        text = text.substring(startIndex, endIndex + 1).trim();

        logger.debug("Extracting JSON array from response text.");
        return objectMapper.readValue(text, new TypeReference<List<MarketingPost>>() {});
    }

}