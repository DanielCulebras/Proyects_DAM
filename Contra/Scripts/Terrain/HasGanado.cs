using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class HasGanado : MonoBehaviour
{
    public GameObject chest;
    public GameObject texto;

    // Update is called once per frame
    void Update()
    {
        if (GetComponent<Text>().IsActive() && chest)
        {
            if (Input.GetKeyDown(KeyCode.E))
            {
                Destroy(chest);
                texto.SetActive(true);
                GetComponent<Text>().enabled = false;
            }
        }
    }
}
