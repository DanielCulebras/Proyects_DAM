using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Turret : MonoBehaviour
{
    public enum State
    {
        Wardering,
        Attacking
    }

    public State state;

    public Transform platform;

    public TextMesh text;

    public Transform player;
    public float detectionDistance = 20f;

    public GameObject bullet;
    public Transform shotSpawn;
    public float fireRate = 4f;
    float timeUntilNextShot;

    public GameObject showState;
    public Material ward;
    public Material attacking;

    // Start is called before the first frame update
    void Start()
    {
        state = State.Wardering;
    }

    // Update is called once per frame
    void Update()
    {
        //CheckIfInRange();
        switch (state)
        {
            case State.Wardering:
                showState.GetComponent<MeshRenderer>().material = ward;
                text.text = "Ward";
                StartCoroutine(Ward());
                break;
            case State.Attacking:
                showState.GetComponent<MeshRenderer>().material = attacking;
                text.text = "Attacking";
                StopCoroutine(Ward());
                Attack();
                break;
        }
    }

    void CheckIfInRange()
    {
        Vector3 distance = transform.position - player.position;
        distance.y = 0;
        float distanceFrom = distance.magnitude;
        distance /= distanceFrom;

        if (distanceFrom < detectionDistance)
        {
            state = State.Attacking;
        }
        else
        {
            state = State.Wardering;
        }
    }

    IEnumerator Ward()
    {
        state = State.Wardering;
        yield return new WaitForSeconds(2);
        Quaternion.RotateTowards(transform.rotation, GetNewLook(), 15f);
        //CheckIfInRange();
    }

    Quaternion GetNewLook()
    {
        float y = Random.Range(30f, 90f);
        Quaternion look = Quaternion.Euler(0f, y, 0f);
        return look;
    }

    void Attack()
    {
        //CheckIfInRange();

        transform.LookAt(player);
        if (Time.time > timeUntilNextShot)
        {
            timeUntilNextShot = Time.time + fireRate;
            Instantiate(bullet, shotSpawn.position, bullet.transform.rotation);
        }
    }


}
