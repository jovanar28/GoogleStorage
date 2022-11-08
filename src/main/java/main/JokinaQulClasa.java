package main;

import baluni.filestorage.MyFileStorage;
import baluni.model.Fajl;

import java.util.List;

public class JokinaQulClasa extends MyFileStorage{

    @Override
    public boolean createStorage(String s) {
        return false;
    }

    @Override
    public boolean createDirectory(String s, String s1) {
        return false;
    }

    @Override
    public boolean createFile(String s, String s1) {
        return false;
    }

    @Override
    public void buildPath(String s) {

    }

    @Override
    public void fileUpload(String s, List<Fajl> list) {

    }

    @Override
    public void deleteFiles(List<Fajl> list) {

    }

    @Override
    public void deleteDirectories(List<Fajl> list) {

    }

    @Override
    public void moveFiles(String s, String s1) {

    }

    @Override
    public void download(String s, String s1) {

    }

    @Override
    public boolean rename(String s, String s1) {
        return false;
    }

    @Override
    public List<Fajl> listFilesInDir(String s) {
        return null;
    }

    @Override
    public List<Fajl> listFilesInSubDir(String s) {
        return null;
    }

    @Override
    public List<Fajl> listFiles(String s) {
        return null;
    }

    @Override
    public List<Fajl> listFilesForExtension(String s) {
        return null;
    }

    @Override
    public List<Fajl> listFilesForName(String s) {
        return null;
    }

    @Override
    public boolean listDirForNames(String s, List<String> list) {
        return false;
    }

    @Override
    public Fajl findDirectoryOfFile(String s) {
        return null;
    }

    @Override
    public List<Fajl> sort(List<Fajl> list, boolean b, boolean b1, boolean b2, boolean b3) {
        return null;
    }

    @Override
    public List<Fajl> listFileByDate(String s, String s1) {
        return null;
    }

    @Override
    public List<Fajl> listFilesBetweenDates(String s, String s1, String s2) {
        return null;
    }

    @Override
    public List<Fajl> filterData(List<Fajl> list, boolean b, boolean b1, boolean b2, boolean b3, boolean b4, boolean b5) {
        return null;
    }
}
