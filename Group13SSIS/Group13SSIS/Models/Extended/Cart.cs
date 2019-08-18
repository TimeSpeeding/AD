using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Group13SSIS.Models
{
    public class Cart
    {
        private List<CartLine> lineCollection = new List<CartLine>();
        public void AddItem(Stationery stationery, int quantity)
        {
            CartLine line = lineCollection.Where(p => p.Stationery.StationeryId == stationery.StationeryId).FirstOrDefault();
            if (line == null)
            {
                lineCollection.Add(new CartLine() { Stationery = stationery, Qty = quantity });
            }
            else
            {
                line.Qty += quantity;
            }
        }
        public void IncreaseOrDecreaseOne(Stationery stationery, int quantity)
        {
            CartLine line = lineCollection.Where(p => p.Stationery.StationeryId == stationery.StationeryId).FirstOrDefault();
            if (line != null)
            {
                line.Qty = quantity;
            }
        }
        public void RemoveLine(Stationery stationery)
        {
            lineCollection.RemoveAll(p => p.Stationery.StationeryId == stationery.StationeryId);
        }
        public double ComputeTotalPrice()
        {
            return lineCollection.Sum(p => p.Stationery.Price * p.Qty);
        }
        public void Clear()
        {
            lineCollection.Clear();
        }
        public List<CartLine> GetList()
        {
            return lineCollection;
        }
    }
}