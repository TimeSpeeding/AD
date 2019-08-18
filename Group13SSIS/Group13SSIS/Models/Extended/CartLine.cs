using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Group13SSIS.Models
{
    public class CartLine
    {
        public Stationery Stationery { get; set; }
        public int Qty { get; set; }
    }
}