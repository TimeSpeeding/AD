using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.ComponentModel.DataAnnotations;

namespace Group13SSIS.Models
{
    public class SupplyDetailVM
    {
        public int SupplyDetailId { get; set; }
        [Display(Name = "Stationery:")]
        [Required(AllowEmptyStrings = false, ErrorMessage = "Please select a stationery!")]
        public string StationeryId { get; set; }
        [Display(Name = "First Supplier:")]
        [Required(AllowEmptyStrings = false, ErrorMessage = "Please select a supplier!")]
        public string FirstSupplierId { get; set; }
        [Display(Name = "SecondSupplier:")]
        [Required(AllowEmptyStrings = false, ErrorMessage = "Please select a supplier!")]
        public string SecondSupplierId { get; set; }
        [Display(Name = "Third Supplier:")]
        [Required(AllowEmptyStrings = false, ErrorMessage = "Please select a supplier!")]
        public string ThirdSupplierId { get; set; }
    }
}