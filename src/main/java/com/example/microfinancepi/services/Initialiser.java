package com.example.microfinancepi.services;

import com.example.microfinancepi.entities.DataPoint;

import java.util.List;

public interface Initialiser {
    List<DataPoint> createInitialCentroids(int k, List<DataPoint> points);
}