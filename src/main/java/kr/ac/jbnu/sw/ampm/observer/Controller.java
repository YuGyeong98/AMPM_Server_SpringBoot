package kr.ac.jbnu.sw.ampm.observer;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class Controller {
    private static HashMap<String, HashMap<String, Object>> testDBHashMap = new HashMap<String, HashMap<String, Object>>();

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> getAllResponseEntity(HttpServletRequest request){
        ResponseEntity<?> responseEntity = null;

        if(!testDBHashMap.isEmpty()){
            responseEntity = new ResponseEntity<>(testDBHashMap, HttpStatus.OK);
        }else{
            responseEntity = new ResponseEntity<>("NOT_FOUND", HttpStatus.NOT_FOUND);
        }
        return responseEntity;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> getResponseEntity(HttpServletRequest request, @PathVariable String id){
        ResponseEntity<?> responseEntity = null;

        if(!testDBHashMap.isEmpty()){
            if(id != null && !id.equals("") && testDBHashMap.containsKey(id)){
                responseEntity = new ResponseEntity<>(testDBHashMap.get(id), HttpStatus.OK);
            }else{
                responseEntity = new ResponseEntity<>("NOT_FOUND", HttpStatus.NOT_FOUND);
            }
        }else{
            responseEntity = new ResponseEntity<>("NOT_FOUND", HttpStatus.NOT_FOUND);
        }
        return responseEntity;
    }

    @RequestMapping(value = "/{id}/{field}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> getFieldResponseEntity(HttpServletRequest request, @PathVariable String id, @PathVariable String field){
        ResponseEntity<?> responseEntity = null;
        HashMap<String, Object> hashMap = new HashMap<String, Object>();

        if(!testDBHashMap.isEmpty()){
            if(id != null && !id.equals("") && testDBHashMap.containsKey(id)){
                if(field != null && !field.equals("") && testDBHashMap.get(id).containsKey(field)){
                    hashMap.put(field, testDBHashMap.get(id).get(field));
                    responseEntity = new ResponseEntity<>(hashMap, HttpStatus.OK);
                }else{
                    responseEntity = new ResponseEntity<>("NOT_FOUND", HttpStatus.NOT_FOUND);
                }
            }else{
                responseEntity = new ResponseEntity<>("NOT_FOUND", HttpStatus.NOT_FOUND);
            }
        }else{
            responseEntity = new ResponseEntity<>("NOT_FOUND", HttpStatus.NOT_FOUND);
        }
        return responseEntity;
    }

    @RequestMapping(value = "/post/{id}", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @SuppressWarnings("unchecked")
    public ResponseEntity<?> postResponseEntity(HttpServletRequest request, @PathVariable String id, @RequestBody Map<String, Object> requestMap){
        ResponseEntity<?> responseEntity = null;
        HashMap<String, Object> postValueHashMap;

        if(id != null && !id.equals("")){
            if(testDBHashMap.containsKey(id)){
                postValueHashMap = testDBHashMap.get(id);
            }else{
                postValueHashMap = new HashMap<String, Object>();
            }
            postValueHashMap.put(id, requestMap);
            Object object = postValueHashMap.get(id);
            if(testDBHashMap.containsKey(id)){
                testDBHashMap.replace(id, (HashMap<String, Object>) object);
            }else{
                testDBHashMap.put(id, (HashMap<String, Object>) object);
            }
            responseEntity = new ResponseEntity<>(requestMap, HttpStatus.OK);
        }else{
            responseEntity = new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    @RequestMapping(value = "/post/{id}/music", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @SuppressWarnings("unchecked")
    public ResponseEntity<?> postMusicResponseEntity(HttpServletRequest request, @PathVariable String id, @RequestBody Map<String, Object> requestMap){
        ResponseEntity<?> responseEntity = null;
        LinkedHashMap<String, Object> musicHashMap;
        HashMap<String, HashMap<String, Object>> music = new HashMap<String, HashMap<String, Object>>();

        if(id != null && !id.equals("")){
            if(testDBHashMap.containsKey(id)){
                if(testDBHashMap.get(id).containsKey("music")){
                    musicHashMap = (LinkedHashMap<String, Object>) testDBHashMap.get(id).get("music");
                }else{
                    testDBHashMap.get(id).put("music", requestMap);
                    musicHashMap = new LinkedHashMap<String, Object>();
                }
                for(Map.Entry<String, Object> entry : requestMap.entrySet()){
                    musicHashMap.put(entry.getKey() + (musicHashMap.size()+1), entry.getValue());
                }
                testDBHashMap.get(id).replace("music", musicHashMap);
                music.put("music", musicHashMap);
                responseEntity = new ResponseEntity<>(music, HttpStatus.OK);
            }else{
                responseEntity = new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
            }
        }else{
            responseEntity = new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    @RequestMapping(value = "/post/{id}/desc", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    @SuppressWarnings("unchecked")
    public ResponseEntity<?> postDescResponseEntity(HttpServletRequest request, @PathVariable String id, @RequestBody Map<String, Object> requestMap){
        ResponseEntity<?> responseEntity = null;
        HashMap<String, HashMap<String, Object>> descHashMap;
        LinkedHashMap<String, Object> subsequentHashMap = new LinkedHashMap<String, Object>();
        HashMap<String, HashMap<String, HashMap<String, Object>>> description = new HashMap<String, HashMap<String, HashMap<String, Object>>>();

        if(id != null && !id.equals("")){
            if(testDBHashMap.containsKey(id)){
                if(testDBHashMap.get(id).containsKey("description")){
                    descHashMap = (HashMap<String, HashMap<String, Object>>) testDBHashMap.get(id).get("description");
                }else{
                    testDBHashMap.get(id).put("description", requestMap);
                    descHashMap = new HashMap<String, HashMap<String, Object>>();
                }
                for(Map.Entry<String, Object> entry : requestMap.entrySet()){
                    subsequentHashMap.put("id", descHashMap.size()+1);
                    subsequentHashMap.put(entry.getKey(), entry.getValue());
                    descHashMap.put("desc" + (descHashMap.size()+1), subsequentHashMap);
                }
                testDBHashMap.get(id).replace("description", descHashMap);
                description.put("description", descHashMap);
                responseEntity = new ResponseEntity<>(description, HttpStatus.OK);
            }else{
                responseEntity = new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
            }
        }else{
            responseEntity = new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE, produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> deleteResponseEntity(HttpServletRequest request, @PathVariable String id){
        ResponseEntity<?> responseEntity = null;

        if(id != null && !id.equals("")){
            if(testDBHashMap.containsKey(id)){
                testDBHashMap.remove(id);
                responseEntity = new ResponseEntity<>("", HttpStatus.OK);
            }else{
                responseEntity = new ResponseEntity<>("NOT_FOUND", HttpStatus.NOT_FOUND);
            }
        }else{
            responseEntity = new ResponseEntity<>("NOT_FOUND", HttpStatus.NOT_FOUND);
        }
        return responseEntity;
    }

    @RequestMapping(value = "/put/{id}", method = RequestMethod.PUT, produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> putResponseEntity(HttpServletRequest request, @PathVariable String id, @RequestBody Map<String, Object> requestMap){
        ResponseEntity<?> responseEntity = null;
        HashMap<String, Object> postValueHashMap = null;

        if(!testDBHashMap.isEmpty()){
            if(id != null && !id.equals("") && testDBHashMap.containsKey(id)){
                postValueHashMap = testDBHashMap.get(id);
                for(Map.Entry <String, Object> entry : postValueHashMap.entrySet()){
                    if(requestMap.containsKey(entry.getKey())){
                        postValueHashMap.put(entry.getKey(), requestMap.get(entry.getKey()));
                        testDBHashMap.replace(id, postValueHashMap);
                        responseEntity = new ResponseEntity<>(requestMap, HttpStatus.OK);
                    }else{
                        responseEntity = new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
                    }
                }
            }else{
                responseEntity = new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
            }
        }else{
            responseEntity = new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }
}
