using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Puerta : MonoBehaviour
{
    bool opening;
    bool cerrada;
    private Vector3 startPos;

    private void Start()
    {
        opening = false;
        cerrada = false;
        startPos = transform.position;
    }

    // Update is called once per frame
    void Update()
    {
        if (GameManager.Instance.targets <= 0)
        {
            opening = true;
            if (opening && transform.position.y < startPos.y + 4 && !cerrada)
                OpenSesame();
        }
    }

    void OpenSesame()
    {
        transform.position += Vector3.up * Time.deltaTime;
    }

    public void CierraQueEntraFrio()
    {
        cerrada = true;
        transform.position = startPos;
    }
}
