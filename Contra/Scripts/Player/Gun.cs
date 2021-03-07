using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Gun : MonoBehaviour
{
    public float dmg = 10f;
    public float range = 5f;

    public float fireRate = 0.11f;
    private float timeUntilNextShot;

    public Camera cam;

    Animator anim;

    private void Start()
    {
        anim = GetComponent<Animator>();
    }

    // Update is called once per frame
    void Update()
    {
        if (Input.GetButtonDown("Fire1") && Time.time > timeUntilNextShot)
        {
            timeUntilNextShot = Time.time + fireRate;
            Shoot();
        }
    }

    void Shoot()
    {
        // Play muzzleFlash (animacionAtaque)
        //GetComponentInChildren<MeshRenderer>().material.color.gamma = 5;

        anim.SetBool("Shooting", true);

        RaycastHit hit;
        Debug.DrawRay(cam.transform.position, cam.transform.forward, Color.yellow);
        if (Physics.Raycast(cam.transform.position, cam.transform.forward, out hit, range, 9))
        {
            Target target = hit.transform.GetComponent<Target>();
            if (target != null)
            {
                target.TakeDamage(dmg);
            }

            Exit exit = hit.transform.GetComponent<Exit>();

            if (exit != null)
            {
                exit.MainMenu();
            }

            // Hit anim
            //Instantiate(impactEffect, hit.point, Quaternion.LookRotation(hit.normal));
        }
    }

    void StopAnim()
    {
        anim.SetBool("Shooting", false);
    }
}
