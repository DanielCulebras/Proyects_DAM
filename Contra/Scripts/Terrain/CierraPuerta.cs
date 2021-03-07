using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class CierraPuerta : MonoBehaviour
{
    public GameObject puerta;

    private void OnTriggerEnter(Collider other)
    {
        if (other.tag == "Player")
            puerta.GetComponent<Puerta>().CierraQueEntraFrio();
    }
}
