package com.example.microfinancepi.entities;

import com.example.microfinancepi.services.Initialiser;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomInitialiser implements Initialiser {
    //placer d'une maniére aléatoire les centres de classe
    //me retourne la liste des point par cluster
    @Override
    public List<DataPoint> createInitialCentroids(int k, List<DataPoint> points) {
        Random generator = new Random();
        List<DataPoint> list = new ArrayList<>();

        while (list.size() < k) {
            int i = generator.nextInt(points.size());
            list.add(points.get(i));
        }

        return list;
    }
}