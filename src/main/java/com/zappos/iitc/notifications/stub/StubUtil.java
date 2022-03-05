package com.zappos.iitc.notifications.stub;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zappos.iitc.notifications.stub.domain.StubResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author RaviGupta
 * This class is meant for providing the utility methods for stubs
 */
public class StubUtil {

    private static List<String> validTokens;

    public static <T> List<T> getResponse(StubResponse<T> stubResponse, Class clazz) {

        List<T> list=null;
        ObjectMapper mapper = new ObjectMapper();
        JavaType type = mapper.getTypeFactory().
                constructCollectionType(List.class, clazz);
        TypeReference<List<? extends T>> typeReference = new TypeReference<List<? extends T>>(){};
        String response = stubResponse.getResponse();
        try {
            list = response!= null ? mapper.readValue(response,type) : null;
        } catch (IOException e){
            e.printStackTrace();
        }
        return list;
    }

    public static <T> List<T> getResponsePaginated(StubResponse<T> stubResponse, Class clazz, int offset, int limit) {

        List<T> list = getResponse(stubResponse,clazz);
        if(list != null && offset <= list.size()){
            int lastIndex = offset+limit+1; //lastIndex is exclusive

            lastIndex = limit == -1 ? list.size() :
                    lastIndex < list.size() ? lastIndex : list.size();
            return list.subList(offset,lastIndex );
        }
        return  null;
    }

    /**
     * Dummy method to mock the authentication of token
     *
     * @param xAuthToken
     * @throws IOException
     */
    public static boolean authenticateToken(String xAuthToken) throws IOException {
        if(validTokens == null){
            validTokens = new ArrayList<>();
            ClassLoader classLoader = StubUtil.class.getClassLoader();
            InputStream stream = classLoader.getResourceAsStream(("requestTokenValidator.txt"));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                validTokens.add(line);
            }
        }
        return validTokens.contains(xAuthToken);
    }
}
