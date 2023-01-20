import java.net.URI;
import java.io.IOException;

class SHandler implements URLHandler {
    // The one bit of state on the server: a number that will be manipulated by
    // various requests.
    String[] elements;
    
    public String[] resize(String[] sa){
        String[] toReturn = new String[sa.length + 1];
        for(int i = 0; i < sa.length; i++){
            toReturn[i] = sa[i];
        }
        return toReturn;
    }

    public String search(String[] sa, String s){
        String toReturn = "";
        for(int i = 0; i < sa.length; i++){
            if(sa[i].contains(s)){
                toReturn += sa[i] + "\n";
            }
        }
        return toReturn;
    }

    public String handleRequest(URI url) {
        if (url.getPath().equals("/")) {
            String s = "";
            if(elements == null){return "Our current storage is empty.";}
            else{
                for(int i = 0; i < elements.length; i++){
                s += elements[i] + "\n";
                }
                return "Our current storage is listed below:\n" + s;
            }   
        } 
        else {
            System.out.println("Path: " + url.getPath());
            if (url.getPath().contains("/add")) {
                String[] parameters = url.getQuery().split("=");
                if (parameters[0].equals("s")) {
                    if(elements == null){
                        elements = new String[1];
                        elements[0] = parameters[1];
                        return "String stored!";
                    }
                    else{
                        String[] inter = resize(elements);
                        inter[elements.length] = parameters[1];
                        elements = inter;
                        return "String stored!";
                    }    
                }
            }
            else if(url.getPath().contains("/search")){
                String[] parameters2 = url.getQuery().split("=");
                if(parameters2[0].equals("s")){
                    if(elements == null){
                        return "You have not stored anything yet.";
                    }
                    else{
                        String s = search(elements, parameters2[1]);
                        if(s.equals("")){
                            return "Nothing was found.";
                        }
                        else{
                            return "Here are the strings that contain " +  parameters2[1] + ":\n" + s;
                        }
                    }
                }
            }
            return "404 Not Found!";
        }
    }
}
class SearchEngine{
    public static void main(String[] args) throws IOException {
        if(args.length == 0){
            System.out.println("Missing port number! Try any number between 1024 to 49151");
            return;
        }

        int port = Integer.parseInt(args[0]);

        Server.start(port, new SHandler());
    }
}

