using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public sealed class GameManager
{
    private static GameManager instance = null;
    private static readonly object padlock = new object();

    GameManager()
    {
        targets = 4;
    }

    public static GameManager Instance
    {
        get
        {
            lock (padlock)
            {
                if (instance == null)
                {
                    instance = new GameManager();
                }
                return instance;
            }
        }
    }

    public int targets;

    public static bool isAlive = true;
}