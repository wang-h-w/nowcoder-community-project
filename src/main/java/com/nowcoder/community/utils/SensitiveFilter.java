package com.nowcoder.community.utils;

import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
public class SensitiveFilter {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    private final TrieNode rootNode = new TrieNode();

    @PostConstruct
    public void init() {
        try (
                InputStream inputStream = getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        ) {
            String keyword;
            while ((keyword = bufferedReader.readLine()) != null) {
                this.addKeyword(keyword);
            }

        } catch (IOException e) {
            logger.error("加载敏感词文件失败：" + e.getMessage());
        }
    }

    /**
     * 过滤敏感词
     * @param text 待过滤的文本
     * @return 过滤后的文本
     */
    public String filter(String text) {
        if (StringUtils.isBlank(text)) return null;
        TrieNode currNode = rootNode;
        int l = 0, r = 0, n = text.length();
        StringBuilder sb = new StringBuilder();
        while (r < n) {
            char c = text.charAt(r);
            if (isSymbol(c)) {
                // 如果当前符号已经在某次查询过程中，则左节点不能移动（依然表示这个词的开头）
                if (currNode == rootNode) {
                    sb.append(c);
                    l++;
                }
                r++;
                continue;
            }
            currNode = currNode.getSubNode(c);
            if (currNode == null) {
                sb.append(text.charAt(l));
                r = ++l;
                currNode = rootNode;
            } else if (currNode.isKey()) {
                sb.append(CommunityConstant.REPLACEMENT);
                l = ++r;
                currNode = rootNode;
            } else {
                r++;
            }
        }
        sb.append(text.substring(l));
        return sb.toString();
    }

    private boolean isSymbol(Character c) {
        // 0x2E80~0x9FFF是东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }

    private void addKeyword(String keyword) {
        TrieNode temp = rootNode;
        for (int i = 0; i < keyword.length(); i++) {
            char c = keyword.charAt(i);
            TrieNode subNode = temp.getSubNode(c);
            if (subNode == null) {
                subNode = new TrieNode();
                temp.addSubNode(c, subNode);
            }
            temp = subNode;
            if (i == keyword.length() - 1) temp.setKey(true);
        }
    }

    private static class TrieNode {
        private boolean isKey = false;
        private final Map<Character, TrieNode> subNodes = new HashMap<>();

        public boolean isKey() {
            return isKey;
        }

        public void setKey(boolean b) {
            isKey = b;
        }

        public void addSubNode(Character c, TrieNode node) {
            subNodes.put(c, node);
        }

        public TrieNode getSubNode(Character c) {
            return subNodes.get(c);
        }
    }
}
