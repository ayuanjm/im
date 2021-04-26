package com.yuan.im.util;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Copyright(c) 2018 Sunyur.com, All Rights Reserved.
 * <P></P>
 *
 * @author YuanJiaMin
 * @date 2021/4/26 4:14 下午
 * @description
 */
public class HistorySort {
    public static void main(String[] args) {
        List<History> historyList = new ArrayList<>(10);
        historyList.add(new History(1, 1));
        historyList.add(new History(2, 2));
        historyList.add(new History(1, 3));
        historyList.add(new History(2, 1));
        historyList.add(new History(1, 2));
        historyList.add(new History(2, 3));
        System.out.println(JSON.toJSONString(historyList));

        LinkedList<History> ansList = new LinkedList<>();
        for (int i = 0; i < historyList.size(); ++i) {
            if (i == 0) {
                ansList.add(historyList.get(i));
            } else {
                History curr = historyList.get(i);
                int insertIndex = compareAndExchange(ansList, ansList.size() - 1, curr, i, i);
                // 递归结束后，没有找到msgFrom相同，且小于curr的数据
                if (ansList.size() == i) {
                    if (insertIndex != i) {
                        // curr的msgSeq是ansList 相同msgFrom里面最小的
                        ansList.add(insertIndex, curr);
                    } else {
                        // curr的前面没有msgFrom相同的数据，直接加入ansList最后
                        ansList.add(curr);
                    }
                }
            }
        }
        System.out.println(JSON.toJSONString(ansList));
    }

    /**
     * @param ansList     排序后返回给前端展示有序的list
     * @param index       ansList 最后一个元素的下标
     * @param curr        当前需要插入ansList的元素
     * @param insertIndex 记录递归结束后，如果没有找到相同msgFrom而且小于curr的元素，最后一个相同msgFrom比curr大的下标
     * @param otherIndex  最后一个msgFrom相同且小于curr的元素
     *                    [{"msgFrom":1,"msgSeq":1},{"msgFrom":2,"msgSeq":2},{"msgFrom":1,"msgSeq":3},
     *                    {"msgFrom":2,"msgSeq":1},{"msgFrom":1,"msgSeq":2},{"msgFrom":2,"msgSeq":3}]
     *                    比如上面的数据，「1，2」应该插入「1，3」前面，而不是「2，2」前面；
     * @return 递归结束后，没有找到msgFrom相同，且小于curr的元素，返回值就是需要插入的位置
     */
    private static int compareAndExchange(LinkedList<History> ansList, int index, History curr, int insertIndex, int otherIndex) {
        History pre = ansList.get(index);
        if (pre.msgFrom == curr.msgFrom) {
            insertIndex = index;
            // 类似插入排序，前面一个比当前小，那么前面所有都比当前小，直接插入pre后面
            if (pre.msgSeq < curr.msgSeq) {
                ansList.add(otherIndex, curr);
                return -1;
            } else {
                if (--index >= 0) {
                    insertIndex = compareAndExchange(ansList, index, curr, insertIndex, insertIndex);
                }
            }
        } else {
            if (--index >= 0) {
                insertIndex = compareAndExchange(ansList, index, curr, insertIndex, otherIndex);
            }
        }
        return insertIndex;
    }

    static class History {
        int msgFrom;
        int msgSeq;

        public int getMsgFrom() {
            return msgFrom;
        }

        public void setMsgFrom(int msgFrom) {
            this.msgFrom = msgFrom;
        }

        public int getMsgSeq() {
            return msgSeq;
        }

        public void setMsgSeq(int msgSeq) {
            this.msgSeq = msgSeq;
        }

        public History(int msgFrom, int msgSeq) {
            this.msgFrom = msgFrom;
            this.msgSeq = msgSeq;
        }
    }
}
