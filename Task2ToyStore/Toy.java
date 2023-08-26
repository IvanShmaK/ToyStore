package org.Task2ToyStore;

public class Toy { ;
    public String id, name, worth;

    /**
     * Это конструктор для класса игрушка
     * @param id это id игрушки
     * @param name это имя игрушки
     * @param worth это вероятность выпадения игрушки (в %, может быть 10, 20 или 30)
     */
    public Toy(String id, String name, String worth){
        this.id = id;
        this.name = name;
        this.worth = worth;
    }

    /**
     * Это метод получения информации об экземпляре класса Toy
     * @return строку с данными игрушки
     */
    public String getInfo() {
        return String.format("Игрушка: %s, id: %s, вес: %s", this.name, this.id, this.worth);
    }
}





