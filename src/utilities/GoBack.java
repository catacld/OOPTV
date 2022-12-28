//package utilities;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import data.Database;
//import factory.Factory;
//import ioclasses.Writer;
//import pages.Page;
//
//import java.util.ArrayList;
//
//public class GoBack {
//
//
//    public static Page back() {
//
//        // check if the user can go back
//        if (CheckAction.canGoBack()) {
//            // pop the current page the user is on
//            // from the stack
//            String currentPage = Database.getInstance().getHistory().pop();
//            // the last page the user has viewed
//            String lastPage = Database.getInstance().getHistory().pop();
//
//            // add the current page back im the history stack
//            //Database.getInstance().getHistory().push(currentPage);
//
//            if (lastPage.equals("see details")) {
//
//                String movieTitle = Database.getInstance().getMovieTitles().pop();
//                return Factory.previousPage(lastPage, movieTitle);
//            }
//            else {
//                return Factory.previousPage(lastPage, null);
//            }
//        } else {
//            Writer.getInstance().addOutput("Error", new ArrayList<>(),
//                    null);
//            return null;
//        }
//    }
//}
