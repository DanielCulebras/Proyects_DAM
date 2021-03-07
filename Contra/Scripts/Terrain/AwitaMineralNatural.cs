using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class AwitaMineralNatural : MonoBehaviour
{

    private void OnTriggerEnter(Collider other)
    {
        if (other.tag == "Player")
        {
            GameManager.isAlive = false;
        }
    }

}
