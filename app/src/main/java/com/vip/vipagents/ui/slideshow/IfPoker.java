package com.vip.vipagents.ui.slideshow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class IfPoker {
    private String[] numbers = {"7", "8", "9", "10", "J", "Q", "K", "A"};

    public boolean isFiveFlush(Poker[] pokers) {
        String back = "null", content = "null";
        for (int i = 0; i < pokers.length; i++) {
            if (i == 0) {
                content = pokers[i].getContent();
                back = pokers[i].getBackground();
                continue;
            }
            if (!pokers[i].getContent().equals(content) || !pokers[i].getBackground().equals(back)) {
                return false;
            }
        }
        return true;
    }

    public boolean isBackStraightFlush(Poker[] pokers) {
        boolean isSave = false;
        String back = "null";
        ArrayList<String> list = new ArrayList<String>();
        String[] contents = {"A", "7", "8", "9", "10"};
        for (int i = 0; i < contents.length; i++) {
            list.add(contents[i]);
        }
        for (int i = 0; i < pokers.length; i++) {
            if (i == 0) {
                back = pokers[i].getBackground();
            }
            if (!pokers[i].getBackground().equals(back)) return false;
            for (int j = 0; j < list.size(); j++) {
                if (list.get(j).equals(pokers[i].getContent())) {
                    list.remove(pokers[i].getContent());
                    if (list.isEmpty()) isSave = true;
                    break;
                }
            }
        }
        if (isSave) return true;
        else return false;
    }

    public boolean isOnePair(Poker[] pokers) {
        Map<String, Integer> maps = new HashMap<String, Integer>();
        for (int i = 0; i < pokers.length; i++) {
            if (!maps.containsKey(pokers[i].getContent())) {
                maps.put(pokers[i].getContent(), 1);
            } else {
                maps.put(pokers[i].getContent(), maps.get(pokers[i].getContent()) + 1);
            }
        }
        if (maps.containsValue(2)) return true;
        else return false;
    }

    public boolean isTwoPair(Poker[] pokers) {
        Map<String, Integer> maps = new HashMap<String, Integer>();
        for (int i = 0; i < pokers.length; i++) {
            if (!maps.containsKey(pokers[i].getContent())) {
                maps.put(pokers[i].getContent(), 1);
            } else {
                maps.put(pokers[i].getContent(), maps.get(pokers[i].getContent()) + 1);
            }
        }
        int count = 0;
        for (Map.Entry<String, Integer> entry : maps.entrySet()) {
            if (entry.getValue() == 2) count++;
        }
        if (count == 2) return true;
        else return false;
    }

    public boolean isTriple(Poker[] pokers) {
        Map<String, Integer> maps = new HashMap<String, Integer>();
        for (int i = 0; i < pokers.length; i++) {
            if (!maps.containsKey(pokers[i].getContent())) {
                maps.put(pokers[i].getContent(), 1);
            } else {
                maps.put(pokers[i].getContent(), maps.get(pokers[i].getContent()) + 1);
            }
        }
        if (maps.containsValue(3)) return true;
        else return false;
    }

    public boolean isFullHouse(Poker[] pokers) {
        Map<String, Integer> maps = new HashMap<String, Integer>();
        for (int i = 0; i < pokers.length; i++) {
            if (!maps.containsKey(pokers[i].getContent())) {
                maps.put(pokers[i].getContent(), 1);
            } else {
                maps.put(pokers[i].getContent(), maps.get(pokers[i].getContent()) + 1);
            }
        }
        if (maps.containsValue(2) && maps.containsValue(3)) return true;
        else return false;
    }

    public boolean isFourCard(Poker[] pokers) {
        Map<String, Integer> maps = new HashMap<String, Integer>();
        for (int i = 0; i < pokers.length; i++) {
            if (!maps.containsKey(pokers[i].getContent())) {
                maps.put(pokers[i].getContent(), 1);
            } else {
                maps.put(pokers[i].getContent(), maps.get(pokers[i].getContent()) + 1);
            }
        }
        if (maps.containsValue(4)) return true;
        else return false;
    }

    public boolean isFlush(Poker[] pokers) {
        String back = "null";
        for (int i = 0; i < pokers.length; i++) {
            if (i == 0) {
                back = pokers[i].getBackground();
            } else {
                if (!pokers[i].getBackground().equals(back)) return false;
            }
        }
        return true;
    }

    public boolean isStraight(Poker[] pokers) {
        boolean isSave = false;
        int position = 0;
        for (int i = 0; i < pokers.length; i++) {
            if (i == 0) {
                for (int j = 0; j < numbers.length; j++) {
                    if (numbers[j].equals(pokers[i].getContent())) {
                        position = j;
                        break;
                    }
                }
            } else {
                for (int j = 0; j < numbers.length; j++) {
                    if (numbers[j].equals(pokers[i].getContent())) {
                        if (j < position) {
                            position = j;
                            break;
                        }
                    }
                }
            }
        }
        if (position > 3) return false;
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < 5; i++) {
            list.add(numbers[position+i]);
        }
        for (int i = 0; i < pokers.length; i++) {
            for (int j = 0; j < list.size(); j++) {
                if (list.get(j).equals(pokers[i].getContent())) {
                    list.remove(pokers[i].getContent());
                    if (list.isEmpty()) isSave = true;
                    break;
                }
            }
        }
        if (isSave) return true;
        else return false;
    }

    public boolean isStraightFlush(Poker[] pokers) {
        boolean isSave = false;
        int position = 0;
        String back = "null";
        for (int i = 0; i < pokers.length; i++) {
            if (i == 0) {
                back = pokers[i].getBackground();
                for (int j = 0; j < numbers.length; j++) {
                    if (numbers[j].equals(pokers[i].getContent())) {
                        position = j;
                        break;
                    }
                }
            } else {
                if (!pokers[i].getBackground().equals(back)) return false;
                for (int j = 0; j < numbers.length; j++) {
                    if (numbers[j].equals(pokers[i].getContent())) {
                        if (j < position) {
                            position = j;
                            break;
                        }
                    }
                }
            }
        }
        if (position > 3) return false;
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < 5; i++) {
            list.add(numbers[position+i]);
        }
        for (int i = 0; i < pokers.length; i++) {
            for (int j = 0; j < list.size(); j++) {
                if (list.get(j).equals(pokers[i].getContent())) {
                    list.remove(pokers[i].getContent());
                    if (list.isEmpty()) isSave = true;
                    break;
                }
            }
        }
        if (isSave) return true;
        else return false;
    }

    public boolean isRoyalStraightFlush(Poker[] pokers) {
        boolean isSave = false;
        String back = "null";
        ArrayList<String> list = new ArrayList<String>();
        String[] contents = {"10", "J", "Q", "K", "A"};
        for (int i = 0; i < contents.length; i++) {
            list.add(contents[i]);
        }
        for (int i = 0; i < pokers.length; i++) {
            if (i == 0) {
                back = pokers[i].getBackground();
            }
            if (!pokers[i].getBackground().equals(back)) return false;
            for (int j = 0; j < list.size(); j++) {
                if (list.get(j).equals(pokers[i].getContent())) {
                    list.remove(pokers[i].getContent());
                    if (list.isEmpty()) isSave = true;
                    break;
                }
            }
        }
        if (isSave) return true;
        else return false;
    }

    public boolean isQueenTop(Poker[] pokers) {
        boolean isQueen = false;
        for (int i = 0; i < pokers.length; i++) {
            if (pokers[i].getContent().equals("Q")) {
                isQueen = true;
                break;
            }
        }
        if (!isQueen) return false;
        else {
            for (int i = 0; i < pokers.length; i++) {
                if (pokers[i].getContent().equals("K") || pokers[i].getContent().equals("A")) {
                    return false;
                }
            }
            return true;
        }
    }

    public boolean isBackStraight(Poker[] pokers) {
        boolean isSave = false;
        ArrayList<String> list = new ArrayList<String>();
        String[] contents = {"A", "7", "8", "9", "10"};
        for (int i = 0; i < pokers.length; i++) {
            for (int j = 0; j < list.size(); j++) {
                if (list.get(j).equals(pokers[i].getContent())) {
                    list.remove(pokers[i].getContent());
                    if (list.isEmpty()) isSave = true;
                    break;
                }
            }
        }
        if (isSave) return true;
        else return false;
    }

    public boolean isMountain(Poker[] pokers) {
        boolean isSave = false;
        ArrayList<String> list = new ArrayList<String>();
        String[] contents = {"10", "J", "Q", "K", "A"};
        for (int i = 0; i < pokers.length; i++) {
            for (int j = 0; j < list.size(); j++) {
                if (list.get(j).equals(pokers[i].getContent())) {
                    list.remove(pokers[i].getContent());
                    if (list.isEmpty()) isSave = true;
                    break;
                }
            }
        }
        if (isSave) return true;
        else return false;
    }

    public boolean isFiveCard(Poker[] pokers) {
        String content = "null";
        for (int i = 0; i < pokers.length; i++) {
            if (i == 0) {
                content = pokers[i].getContent();
                continue;
            }
            if (!content.equals(pokers[i].getContent())) return false;
        }
        return true;
    }
}
