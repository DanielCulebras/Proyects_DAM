using UnityEngine;

public class Target : MonoBehaviour
{
    public float hp = 50f;

    public void TakeDamage (float amount)
    {
        hp -= amount;
        Debug.Log("Hp: "+hp);
        if (hp <= 0f)
        {
            Die();
        }
    }

    void Die()
    {
        if (tag == "Target")
            GameManager.Instance.targets--;
        Destroy(gameObject);
    }

}
