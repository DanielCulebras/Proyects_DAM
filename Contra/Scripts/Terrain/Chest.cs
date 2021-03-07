using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class Chest : MonoBehaviour
{
    public Text cogerObjeto;
    public Transform player;
    public float detectionDistance = 5f;
    private bool sel = false;

    private void Update()
    {
        Vector3 distance = transform.position - player.position;
        distance.y = 0;
        float distanceFrom = distance.magnitude;
        distance /= distanceFrom;

        if (distanceFrom < detectionDistance && !sel)
        {
            if (cogerObjeto)
            {
                cogerObjeto.enabled = true;
                cogerObjeto.GetComponent<Coger>().chest = gameObject;
                sel = true;
            }
        }
        else if(distanceFrom >= detectionDistance && sel)
        {
            if (cogerObjeto)
            {
                cogerObjeto.enabled = false;
                cogerObjeto.GetComponent<Coger>().chest = null;
                sel = false;
            }
        }
    }
}
