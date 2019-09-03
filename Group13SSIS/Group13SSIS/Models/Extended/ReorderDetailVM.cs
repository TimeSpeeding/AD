using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Group13SSIS.Models
{
    public class ReorderDetailVM
    {
        public int ReorderDetailId { get; set; }
        public int ReorderId { get; set; }
        public Stationery Stationery { get; set; }
        public int Qty { get; set; }
    }
}