using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class Coger : MonoBehaviour
{
    public GameObject chest;
    public GameObject arma;

    // Update is called once per frame
    void Update()
    {
        if (GetComponent<Text>().IsActive() && chest)
        {
            if (Input.GetKeyDown(KeyCode.E))
            {
                Destroy(chest);
                arma.GetComponent<Gun>().dmg += 1;
                GetComponent<Text>().enabled = false;
            }
        }
    }
}
