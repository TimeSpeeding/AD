using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.ComponentModel.DataAnnotations;

namespace Group13SSIS.Models
{
    [MetadataType(typeof(SupplierValidation))]
    public partial class Supplier
    {
    }
    public class SupplierValidation
    {
        public int SupplierId { get; set; }
        [Display(Name = "Suppliercode:")]
        [Required(AllowEmptyStrings = false, ErrorMessage = "Please enter supplier code!")]
        public string Code { get; set; }
        [Display(Name = "SupplierName:")]
        [Required(AllowEmptyStrings = false, ErrorMessage = "Please enter supplier name!")]
        public string Name { get; set; }
        [Display(Name = "GSTNumber:")]
        [Required(AllowEmptyStrings = false, ErrorMessage = "Please enter GST number!")]
        public string GSTNo { get; set; }
        [Display(Name = "ContactName:")]
        [Required(AllowEmptyStrings = false, ErrorMessage = "Please enter Contact Name!")]
        public string ContactName { get; set; }
        [Display(Name = "PhoneNumber:")]
        [RegularExpression(@"^\d{3}\-\d{4}", ErrorMessage = "Please enter the number in accurate pattern!")]
        [Required(AllowEmptyStrings = false, ErrorMessage = "Please enter a Phone Number!")]
        public string PhoneNo { get; set; }
        [Display(Name = "FaxNumber:")]
        [RegularExpression(@"^\d{3}\-\d{4}", ErrorMessage = "Please enter the number in accurate pattern!")]
        [Required(AllowEmptyStrings = false, ErrorMessage = "Please enter a Fax Number!")]
        public string FaxNo { get; set; }
        [Display(Name = "Address:")]
        [Required(AllowEmptyStrings = false, ErrorMessage = "Please select a Address!")]
        public string Address { get; set; }
    }
}