using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class PlayerMovement : MonoBehaviour
{
    public float speed = 12f;
    public float gravity = -9.81f;
    public float jumpHeight = 3f;
    public Transform groundCheck;
    public float groundDist = 0.4f;
    public LayerMask groundMask;

    Vector3 velocity;
    bool isGrounded;

    private CharacterController charControler;

    // Start is called before the first frame update
    void Start()
    {
        charControler = GetComponent<CharacterController>();
        
    }

    // Update is called once per frame
    void Update()
    {
        isGrounded = Physics.CheckSphere(groundCheck.position, groundDist, groundMask);

        if(isGrounded && velocity.y < 0)
        {
            velocity.y = -2f;
        }

        float x = Input.GetAxis("Horizontal");
        float z = Input.GetAxis("Vertical");

        Vector3 move = transform.right * x + transform.forward * z;
        charControler.Move(move * speed * Time.deltaTime);

        if (Input.GetButtonDown("Jump") && isGrounded)
        {
            // v = Sqrt(h * -2 * g)
            velocity.y = Mathf.Sqrt(jumpHeight * -2 * gravity);
        }

        // Gravity
        velocity.y += gravity * Time.deltaTime;

        charControler.Move(velocity * Time.deltaTime);
    }
}
