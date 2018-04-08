package BackEnd.Database;

import java.io.Serializable;

/**
 * Created by ressay on 07/04/18.
 */
public class QueryAnswer implements Serializable
{
    private Product[] products = null;
    public QueryAnswer()
    {
    }
    public QueryAnswer(Product[] products)
    {
        this.products = products;
    }

    public void merge(QueryAnswer queryAnswer)
    {
        int n1 = (getProducts()!=null)?getProducts().length:0;
        int n2 = (queryAnswer.getProducts()!=null)?queryAnswer.getProducts().length:0;
        if(n1+n2==0) return;
        this.products = new Product[n1+n2];
        if(getProducts() != null)
            for (int i = 0; i < getProducts().length; i++) {
                products[i] = getProduct(i);
            }
        if(queryAnswer.getProducts() != null)
            for (int i = getProducts().length; i < queryAnswer.getProducts().length; i++) {
                products[i] = queryAnswer.getProduct(i-getProducts().length);
            }
    }

    public Product getProduct(int index)
    {
        return products[index];
    }

    public Product[] getProducts() {
        return products;
    }

    public void setProducts(Product[] products) {
        this.products = products;
    }
}
