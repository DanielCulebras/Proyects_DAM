using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class BulletController : MonoBehaviour
{
    public float speed = 10f;

    Vector3 spawnCoordinates;

    GameObject player;
    Vector3 lastPosPlayer;
    bool flying;

    Vector3 direction;

    // Start is called before the first frame update
    void Start()
    {
        spawnCoordinates = transform.position;
        player = GameObject.Find("Player");
        lastPosPlayer = player.transform.position + new Vector3(0f, 1f, 0f);
        direction = lastPosPlayer - spawnCoordinates;
        direction = direction.normalized;

    }

    // Update is called once per frame
    void Update()
    {
        float step = speed * Time.deltaTime;

        transform.position += direction * step;
        
    }

    private void OnTriggerEnter(Collider other)
    {
        if (other.tag == "Player")
        {
            Debug.Log("Te han dado");
            GameManager.isAlive = false;
            Destroy(gameObject);
        }
        if (other.tag == "Enviroment")
        {
            Destroy(gameObject);
        }
    }
}
