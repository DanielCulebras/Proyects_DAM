using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.AI;

public class Soldier : MonoBehaviour
{
    public enum State
    {
        Iddle,
        Moving,
        Attacking
    }

    public State state;

    public float speed = 1f;
    public float range = 10f;

    public Transform platform;

    private NavMeshAgent agent;
    private NavMeshPath path;
    public float timeUntilNextPoint = 2f;
    private bool validPath;

    private Vector3 target;

    public TextMesh text;

    public Transform player;
    public float detectionDistance = 20f;

    public GameObject bullet;
    public Transform shotSpawn;
    public float fireRate = 4f;
    float timeUntilNextShot;

    bool test = true;

    public GameObject showState;
    public Material iddle;
    public Material attacking;

    // Start is called before the first frame update
    void Start()
    {
        agent = GetComponent<NavMeshAgent>();
        path = new NavMeshPath();
        state = State.Iddle;
    }

    // Update is called once per frame
    void Update()
    {
        switch (state)
        {
            case State.Iddle:
                showState.GetComponent<MeshRenderer>().material = iddle;
                text.text = "Iddle";
                if (test)
                {
                    StartCoroutine(Patrol());
                    test = false;
                } 
                break;
            case State.Moving:
                text.text = "Moving";
                break;
            case State.Attacking:
                showState.GetComponent<MeshRenderer>().material = attacking;
                text.text = "Attacking";
                StopCoroutine(Patrol());
                Attack();
                break;
        }
    }

    Vector3 NewRandPos()
    {
        float x = Random.Range(platform.position.x - 20, platform.position.x + 20);
        float z = Random.Range(platform.position.z - 20, platform.position.z + 20);

        Vector3 pos = new Vector3(x, 0, z);
        return pos;
    }

    IEnumerator Patrol()
    {
        state = State.Moving;
        yield return new WaitForSeconds(timeUntilNextPoint);
        GetNewPath();
        validPath = agent.CalculatePath(target, path);
        while (!validPath)
        {
            yield return new WaitForSeconds(0.01f);
            GetNewPath();
            validPath = agent.CalculatePath(target, path);
        }
        //CheckIfInRange();
        state = State.Iddle;
    }

    void GetNewPath()
    {
        target = NewRandPos();
        agent.SetDestination(target);
    }
    void CheckIfInRange()
    {
        Vector3 distance = transform.position - player.position;
        distance.y = 0;
        float distanceFrom = distance.magnitude;
        distance /= distanceFrom;

        Debug.Log(distance);

        if (distanceFrom < detectionDistance)
        {
            state = State.Attacking;
        }
        else if(distanceFrom >= detectionDistance)
        {
            state = State.Iddle;
        }
    }

    void Attack()
    {
        //CheckIfInRange();

        transform.LookAt(player);
        //Debug.Log(timeUntilNextShot);
        if (Time.time > timeUntilNextShot)
        {
            timeUntilNextShot = Time.time + fireRate;
            Instantiate(bullet, shotSpawn.position, bullet.transform.rotation);
        }
    }

}

