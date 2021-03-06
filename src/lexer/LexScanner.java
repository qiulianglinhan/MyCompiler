package lexer;

import java.io.*;

public class LexScanner {
    private FileReader fr = null;
    private FileWriter fw = null;
    private PushbackReader pr = null;
    private String outMidFileName;

    public LexScanner(String filename){
        File fin = new File((filename));

        // 文件必须以.c结尾
        if(!fin.getName().endsWith(".c")){
            System.err.println("文件不是以.c结尾的c语言文件，请重新选择文件");
            System.exit(-1);
        }

        // 尝试读取文件
        try {
            fr = new FileReader(fin);
            pr = new PushbackReader(fr,2);  // 两个缓冲字符
        } catch (FileNotFoundException e) {
            System.err.println("未找到"+filename+"文件，请重新输入");
            e.printStackTrace();
        }

        outMidFileName = filename.substring(0,filename.indexOf('.'))+".lex";    // 词法分析结果后缀lex
        File fout = new File(outMidFileName);

        try {
            if(!fout.exists())
                fout.createNewFile();
            fw = new FileWriter(fout);
        } catch (IOException e) {
            System.err.println("写入文件错误");
            e.printStackTrace();
        }

    }

    public String getOutMidFileName() {
        return outMidFileName;
    }

    public boolean closeStream(){
        try {
            fw.close();
            return true;
        } catch (IOException e) {
            System.err.println("关闭流错误！");
            e.printStackTrace();
        }
        return false;
    }

    public Character peek() throws IOException {
        int c = pr.read();
        pr.unread(c);
        return (char) c;
    }

    public Character[] peekTwoChar() throws IOException {
        Character[] chs = new Character[2];
        chs[0] = (char)pr.read();
        chs[1] = (char)pr.read();
        pr.unread(chs[1]);
        pr.unread(chs[0]);
        return chs;
    }

    public Character next() throws IOException {
        return (char)pr.read();
    }

    public void back(int c) throws IOException {
        pr.unread(c);
    }

    public void backChars(char[] buf) throws IOException {
        for (char c : buf) {
            pr.unread(c);
        }
    }

    public void writeToFile(int categoryCode,String tokenName,int line) throws IOException {
        fw.write("["+categoryCode+","+tokenName+","+line+"]\n");
    }

}
