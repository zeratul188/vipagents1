package com.vip.vipagents.ui.slideshow;

public class PokerSet extends IfPoker {
    private String[] name = {"탑", "원페어", "투페어", "트리플", "풀하우스", "스트레이트", "포카드", "플러쉬", "스트레이트 플러쉬", "로얄 스트레이트 플러쉬", "퀸탑", "백 스트레이트", "마운팅", "파이브카드",
            "백 스트레이트 플러쉬", "파이브 플러쉬"};
    private int[] score = {0, 10, 20, 30, 46, 51, 64, 87, 140, 190, 20, 66, 76, 120, 160, 170};

    public PokerResult output(int position) {
        return new PokerResult(score[position], name[position]);
    }

    public int getScore(int position) {
        return score[position];
    }

    public String getName(int position) {
        return name[position];
    }

    public PokerResult outResult(int position) {
        return new PokerResult(score[position], name[position]);
    }

    public int getSize() {
        return name.length;
    }

    public PokerResult checkOption(Poker[] pokers) {
        if (isRoyalStraightFlush(pokers)) return outResult(9);
        else if (isFiveFlush(pokers)) return outResult(15);
        else if (isBackStraightFlush(pokers)) return outResult(14);
        else if (isStraightFlush(pokers)) return outResult(8);
        else if (isFiveCard(pokers)) return outResult(13);
        else if (isFlush(pokers)) return outResult(7);
        else if (isMountain(pokers)) return outResult(12);
        else if (isBackStraight(pokers)) return outResult(11);
        else if (isFourCard(pokers)) return outResult(6);
        else if (isStraight(pokers)) return outResult(5);
        else if (isFullHouse(pokers)) return outResult(4);
        else if (isTriple(pokers)) return outResult(3);
        else if (isTwoPair(pokers)) return outResult(2);
        else if (isOnePair(pokers)) return outResult(1);
        else if (isQueenTop(pokers)) return outResult(10);
        else return outResult(0);
    }


}
