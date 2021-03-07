using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class BridgeFall : MonoBehaviour
{
    public float speed = 2f;
    public float fallDelay = 1f;

    private bool fall, falling;

    private Vector3 startPos;

    private void Start()
    {
        fall = false;
        startPos = transform.position;
    }

    private void Update()
    {
        if (startPos.y - 20 > transform.position.y)
        {
            transform.position = startPos;
            fall = false;
        }
            
        if (fall)
        {
            Fall();
        }
    }

    private void OnTriggerEnter(Collider other)
    {
        if (other.tag == "Player")
        {
            fall = true;
        }
    }

    void Fall()
    {
        transform.position += Vector3.down * speed * Time.deltaTime;
    }

}
