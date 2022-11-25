package main;

import baluni.model.Fajl;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws GeneralSecurityException, IOException {
        GoogleFileStorage jokina = new GoogleFileStorage();
//        jokina.loadExtensions();

      jokina.createStorage("1bjmpZyP8iNLSkgDfd5LB0K7c1uSycU6U");
//      jokina.createDirectories("1TR_01uKnWXkp6et-QhF5dcr2YHvXkpCc","f{1..10}");
        for(Fajl fajl : jokina.listFilesForExtension(".","json"))
            System.out.println(fajl);
//
//      for(Fajl fajl : jokina.listFilesInDir("."))
//          System.out.println(fajl);
//        String line = "";
//
//        System.out.println(jokina.getStorageConfig().getStorageName());
//
//        do{
//            Scanner scanner = new Scanner(System.in);
//
//            System.out.printf("# ");
//            line = scanner.nextLine();
//
//            switch (line){
//                case "setSize":
//                    long storageSize = scanner.nextLong();
//                    jokina.getStorageConfig().setDefaultStorageSize(storageSize);
//                    break;
//                case "setName":
//                    String name = scanner.nextLine();
//                    jokina.getStorageConfig().setStorageName(name);
//                    break;
//                case "setExtensions":
//                    String extensions = scanner.nextLine();
//                    String[] data = extensions.split(",");
//
//                    for(String d : data) {
////                        local.getStorageConfig().getForbiddenExtensions().add(d);
//                        if(!jokina.getStorageConfig().getForbiddenExtensions().contains(d))
//                            jokina.getStorageConfig().getForbiddenExtensions().add(d);
//                    }
//
//                    break;
//                case "config":
//                    jokina.saveStorageConfig(jokina.getSotragePath());
//                    break;
//                case "exit":
//                    System.out.println("Bad command");
//                    break;
//            }
//        }while(!line.equalsIgnoreCase("exit"));


//      jokina.createDirectories("1bjmpZyP8iNLSkgDfd5LB0K7c1uSycU6U","joksim{1..4}");
//      jokina.fileUpload("1896Er-oOba66pRmjGBWrHOIyZ2WjZHWY",List.of(new Fajl("otter","jpg","C:\\Users\\Vid\\Documents\\otter.jpg\\")));
//        System.out.println(JokinaQulClasa.usedSpace);
//        jokina.createDir(".","vid");
//        jokina.createDirectory(".","ogranicen",2);
//        System.out.println(jokina.getStorageConfig().getFoldersWithCapacity());
//        jokina.rename("1fUxIh03XTPMPblBkWVBWp538vWQVbAMM","punk");

//        System.out.println("List Files===================");
//        for(Fajl fajl : jokina.listFiles(""))
//            System.out.println(fajl);
////
//        System.out.println("List FilesForExtension==================");
//        for(Fajl fajl : jokina.listFilesForExtension("1TLvt372imT2mqD45CPLYlLht5INNzpBE","jpg"))
//            System.out.println(fajl);
//
////         MORA FIX
//        System.out.println("List FilesForName=================");
//        for(Fajl fajl : jokina.listFilesForName("1896Er-oOba66pRmjGBWrHOIyZ2WjZHWY","test"))
//            System.out.println(fajl + " -> " + fajl.getExtension());
//
//        System.out.println("Sort===================");
//        for(Fajl fajl : jokina.sort(jokina.listFilesInSubDir(""),true,false,false,true))
//            System.out.println(fajl);
//
//        System.out.println("Filter================");
//        for(Fajl fajl : jokina.filterData(jokina.listFilesInSubDir(""),true,true,false,false,false,false))
//            System.out.println(fajl);
//
//
//
////        Fajl parent = jokina.findDirectoryOfFile("test.txt");
//
////        System.out.println(parent.getFileName());
//
//
//
//
//            jokina.download("116RhzT1bcTs0sJpbSz_w7EZPZWMpWmfg","C:\\Users\\Vid\\Desktop\\");


//
//        jokina.rename("1hLhF3-8wIl5a5cmiDMm-_SKT43pwvR9b","testRename");
//        jokina.download("1hLhF3-8wIl5a5cmiDMm-_SKT43pwvR9b","C:\\Users\\Vid\\Desktop\\");
//
//        List<Fajl> fajlovi = jokina.listFilesInDir("1lB0kEzeVmQCJ43Wmgn9eqp9KXYvS2B7u");
//
//        for(Fajl fajl : fajlovi){
//            System.out.println(fajl);
//        }

//        jokina.deleteFiles(List.of(new Fajl("","folder","1lB0kEzeVmQCJ43Wmgn9eqp9KXYvS2B7u",null,null,0)));
//        jokina.moveFiles("1ndAkrAGIDTQyWkc_3nkpzUIs9MbGfX7S","1DYUl6sReaq7tWJ2Rk9wHpR90EIJmWRpm");

//        List<Fajl> results = jokina.listFilesInDir("1ndAkrAGIDTQyWkc_3nkpzUIs9MbGfX7S");
//
//        for(Fajl fajl : results){
//            System.out.println(fajl.getPath());
//            jokina.download(fajl.getPath(), "C:\\Users\\Vid\\Desktop\\tt\\");
//        }

//        jokina.rename("1do9FWdsv4_wOWkt7zb0nuECtn_NCgcKS","jovanaPrekini");
//        jokina.download("1nwA_GPhVHfAmp206nn4uQP2F2UpWLcmw","C:\\Users\\Vid\\Desktop\\");

//        List<Fajl> fajlovi = jokina.listFilesInDir("1ndAkrAGIDTQyWkc_3nkpzUIs9MbGfX7S");
//
//        for (Fajl fajl : fajlovi) {
//            System.out.println(fajl.getFileSize());
//        }

//        if(jokina.createStorage("1mlQQyzFxfcG3JaAAHQC1qJ2k2xXNVee-")){
////            jokina.createDirectory("1mlQQyzFxfcG3JaAAHQC1qJ2k2xXNVee-","stash", 3);
//            jokina.getStorageConfig().getFoldersWithCapacity().put("1T3uPfO5eoTM1r-knSCSJ6ILojYry5Vru",3);
//            jokina.getStorageConfig().getForbiddenExtensions().add("exe");
//            jokina.getStorageConfig().setDefaultStorageSize(1073741824);
////            jokina.createDirectories("1T3uPfO5eoTM1r-knSCSJ6ILojYry5Vru","joka{1..2}");
//            jokina.rename("1ensg39AP-3zBuot5PW9-WkfZmuesFpte","pokusaj.exe");
////            jokina.moveFiles("1ensg39AP-3zBuot5PW9-WkfZmuesFpte","1T3uPfO5eoTM1r-knSCSJ6ILojYry5Vru");
////            jokina.moveFiles("1ensg39AP-3zBuot5PW9-WkfZmuesFpte","1T3uPfO5eoTM1r-knSCSJ6ILojYry5Vru");
//            jokina.fileUpload("1T3uPfO5eoTM1r-knSCSJ6ILojYry5Vru", List.of(new Fajl("temp_240","txt","C:\\Users\\Vid\\Desktop\\temp_240.txt", LocalDate.now(),LocalDate.now(),15215)));
//            jokina.fileUpload("1T3uPfO5eoTM1r-knSCSJ6ILojYry5Vru", List.of(new Fajl("testRenam","pdf","C:\\Users\\Vid\\Desktop\\testRename.pdf", LocalDate.now(),LocalDate.now(),15215)));
//        }

//        List<Fajl> fajloviSub = jokina.listFilesInSubDir("13fn5WBkrWPicmlk4CPjk04z_I5WfcngO");
//
//        for(Fajl fajl : fajloviSub){
//            System.out.println(fajl);
//        }
//
//        System.out.println("****************");
//
//        List<Fajl> result = jokina.sort(fajloviSub, true, false, false,true);
//
//        for(Fajl fajl : result)
//            System.out.println(fajl + "->" + fajl.getModificationDate());
//
//        List<Fajl> datez = jokina.listFileByDate("2019-10-19","13fn5WBkrWPicmlk4CPjk04z_I5WfcngO");
//
//        for(Fajl fajl : datez){
//            System.out.println(fajl);
    }

//        jokina.download("1ndAkrAGIDTQyWkc_3nkpzUIs9MbGfX7S","C:\\Users\\Vid\\Desktop\\");
//        if(jokina.createStorage("1mlQQyzFxfcG3JaAAHQC1qJ2k2xXNVee-")){
//            System.out.println(jokina.getStorageConfig().getStorageName());
//
//            jokina.createDirectory("1mlQQyzFxfcG3JaAAHQC1qJ2k2xXNVee-", "privatno", 4);
//            System.out.println(jokina.getStorageConfig().getFoldersWithCapacity().keySet());
//            String id = "";
//            for(Fajl fajl : jokina.listDirectoriesForDir("1mlQQyzFxfcG3JaAAHQC1qJ2k2xXNVee-")){
//                 if(fajl.getFileName().equals("privatno")) {
//                     id = fajl.getPath();
//                    break;
//                 }
//             }
//
//            jokina.createDirectories(id,"joka{1..5}");
////            System.out.println(jokina.getStorageConfig().getFoldersWithCapacity().get());
//        }
//        jokina.createStorage("1mlQQyzFxfcG3JaAAHQC1qJ2k2xXNVee-");
//        jokina.createDirectories("1VggI4zYiDL0bZI7Y_QwGq-qQ3uvf8q_A","joka{1..100}");


//        boolean isOke = jokina.createDirectory("","");
//        System.out.println(isOke);
    // jokina.listDrive();
//        jokina.loadExtensions();
    //  List<Fajl> fajlovi = jokina.listFilesForExtension("19QCt7UD9pyAzxVUkTIUPUeSYvVyDWpeN","png");
    // List<Fajl> byNameRes = jokina.listFilesForName("19QCt7UD9pyAzxVUkTIUPUeSYvVyDWpeN", "1");

    //  for(Fajl fajl : fajlovi)
    //    System.out.println(fajl);
    //jokina.listFilesForExtension("pdf");
//        jokina.listFilesForName("ai");
    // JokinaQulClasa.pathsMap.put("root","root");
    //  jokina.listFiles("root");
    // jokina.listFilesInSubDir("root");
    // jokina.rename("19LuvPfmmA93NMBnn0k5aHTOLbSz6uuym","novoImeFoldera");
    // System.out.println(JokinaQulClasa.pathsMap.get("impl4"));
       /* List<Fajl> lista=jokina.listFilesInDir("14_lojv-P6_wLyRt0_o0TOLcIsgQd4GYf");
        List<Fajl> result=jokina.filterData(lista,false,true,false,false,false,false);
        for(Fajl fajl:result)
            System.out.println(fajl.toString());
*/
    //boolean radi=jokina.listDirForNames("14_lojv-P6_wLyRt0_o0TOLcIsgQd4GYf",List.of("bingo.jpg","prvi","trci"));


//        for(Fajl fajl:jokina.listFilesInSubDir("root"))
//            System.out.println(fajl.toString());
//

//        jokina.createDirectories("19QCt7UD9pyAzxVUkTIUPUeSYvVyDWpeN","joka{1..5}");
//
//        List<Fajl> foldersToDelete = new ArrayList<>();
//
//        for(Fajl fajl:jokina.listDirectoriesForDir("19QCt7UD9pyAzxVUkTIUPUeSYvVyDWpeN")) {
//            if (fajl.getFileName().contains("joka")) {
//                foldersToDelete.add(fajl);
//            } else {
//                System.out.println(fajl);
//            }
//        }

//        jokina.deleteDirectories(foldersToDelete);

//        for(Fajl fajl : jokina.listDirectoriesForDir("root"))
//            System.out.println(fajl);
//

//        jokina.download("1AF9-CidA9E1yklDOvumM3qYSWkfSPrz6","C:\\Users\\12345\\Desktop\\GoogleStorage\\");
    //jokina.moveFiles("1ojRV4MpQh-UeqBm1NOMZHXbq5Q5L1lmm2aLQBGtz8Q0","root");
//        jokina.listFilesInDir("1wUrvINPRm-1RGp8eyEv0FRJEFm6EP70W");
    // jokina.rename("14_lojv-P6_wLyRt0_o0TOLcIsgQd4GYf","novo ime");
//        jokina.fileUpload("root",List.of(new Fajl("poor mini kurt","jpg","C:\\Users\\12345\\Desktop\\poor mini kurt.jpg"),
//                new Fajl("otter","jpeg","C:\\Users\\12345\\Desktop\\otter.jpeg")));
    //jokina.deleteFiles(List.of(new Fajl("","","14_lojv-P6_wLyRt0_o0TOLcIsgQd4GYf")));
//        jokina.createStorage("1mohCD8VMij1T1rZKbl2Mve9T3MJxLLHk");
//        jokina.download("1BPQ38bUlI9mohxQfoZ78DsiO7aYbvZbD","C:\\Users\\12345\\Desktop\\");
}
