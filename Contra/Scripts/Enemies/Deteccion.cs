using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Deteccion : MonoBehaviour
{
    public GameObject enemy;

    
    private void Start()
    {
        if (enemy.GetComponent<Turret>())
            GetComponent<SphereCollider>().radius = enemy.GetComponent<Turret>().detectionDistance;
        if (enemy.GetComponent<Soldier>())
            GetComponent<SphereCollider>().radius = enemy.GetComponent<Soldier>().detectionDistance;

    }
    
    private void OnTriggerEnter(Collider other)
    {
        if (other.tag == "Player")
        {
            if (enemy.GetComponent<Turret>())
                enemy.GetComponent<Turret>().state = Turret.State.Attacking;
            if (enemy.GetComponent<Soldier>())
                enemy.GetComponent<Soldier>().state = Soldier.State.Attacking;
        }
    }

    private void OnTriggerExit(Collider other)
    {
        if (other.tag == "Player")
        {
            if (enemy.GetComponent<Turret>())
                enemy.GetComponent<Turret>().state = Turret.State.Wardering;
            if (enemy.GetComponent<Soldier>())
                enemy.GetComponent<Soldier>().state = Soldier.State.Iddle;
        }
    }
}
