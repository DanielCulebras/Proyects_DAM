using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class Dmg : MonoBehaviour
{
    public GameObject gun;

    // Update is called once per frame
    void Update()
    {
        gameObject.GetComponent<Text>().text = "Daño: " + gun.GetComponent<Gun>().dmg;
    }
}
