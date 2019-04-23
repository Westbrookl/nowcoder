package com.nowcoder.wenda.service;

import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jhc on 2019/4/22
 */
@Service
public class SensitiveService implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(SensitiveService.class);
    private static String TEMP = "***";

    private class TrieNode {
        //判断当前节点是否为重点
        private boolean end = false;
        //存储当前节点的子节点
        private Map<Character, TrieNode> map = new HashMap<>();

        //添加子节点
        void addSubNode(Character character, TrieNode node) {
            map.put(character, node);
        }

        //得到对应字母的节点
        TrieNode getSubNode(Character key) {
            return map.get(key);
        }

        //判断当前节点是不是终止条件
        boolean isEndKey() {
            return end;
        }

        //设置最后节点的内容
        void setKeyWordEnd(boolean end) {
            this.end = end;
        }

        //得到子节点的大小
        public int getSubNodeCount() {
            return map.size();
        }

    }

    private TrieNode root = new TrieNode();

    private boolean isSymbol(Character c) {
        int ic = (int) c;
        return !CharUtils.isAsciiAlphanumeric(c) && (ic < 0x2E80 || ic > 0x9FFF);
    }


    public String filter(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        String replace = TEMP;
        StringBuilder sb = new StringBuilder();
        int begin = 0;
        int pos = 0;
        TrieNode nodePos = root;
        while (pos < str.length()) {
            char c = str.charAt(pos);
            if (isSymbol(c)) {
                if (nodePos == root) {
                    sb.append(c);
                    begin++;
                }
                pos++;
                continue;
            }
            nodePos = nodePos.getSubNode(c);
            if (nodePos == null) {
                sb.append(str.charAt(begin));
                pos = begin + 1;
                begin = pos;
                nodePos = root;
            } else if (nodePos.isEndKey()) {
                sb.append(replace);
                pos++;
                begin = pos;
                nodePos = root;
            } else {
                pos++;
            }
        }
        sb.append(str.substring(begin));
        return sb.toString();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            //Thread.currentThread().getContextClassLoader()这个是为了得到当前路径的URL的值，是为了得到Classpath更能方便的读取文件
            InputStream is = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("sensitiveWords.txt");
            InputStreamReader reader = new InputStreamReader(is);
            BufferedReader buffReader = new BufferedReader(reader);
            String lineTxt;
            while ((lineTxt = buffReader.readLine()) != null) {
                lineTxt = lineTxt.trim();
                addWord(lineTxt);
            }
            buffReader.close();
            reader.close();
            is.close();
        } catch (Exception e) {
            logger.error("读取敏感词文件错误", e.getMessage());
        }
    }

    public void addWord(String txt) {
        TrieNode tempNode = root;
        for (int i = 0; i < txt.length(); i++) {
            Character c = txt.charAt(i);
            if (isSymbol(c)) {
                continue;
            }
            TrieNode node = tempNode.getSubNode(c);
            if (node == null) {
                node = new TrieNode();
                tempNode.addSubNode(c, node);
            }
            tempNode = node;
            if (i == txt.length() - 1) {
                tempNode.setKeyWordEnd(true);
            }
        }
    }
//    public static void main(String[] args){
//        SensitiveService ss = new SensitiveService();
//        String[] s = {"色情","赌博"};
//        String s1 =  "你好色情不 要赌  博";
//        for(String str:s){
//            ss.addWord(str);
//        }
//        s1 = ss.filter(s1);
//        System.out.println(s1);
//    }
}
