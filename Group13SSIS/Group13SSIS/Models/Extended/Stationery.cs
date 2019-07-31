using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.ComponentModel.DataAnnotations;

namespace Group13SSIS.Models
{
    [MetadataType(typeof(StationeryValidation))]
    public partial class Stationery
    {

    }
    public class StationeryValidation
    {
        public int StationeryId { get; set; }
        [Display(Name = "Code")]
        [Required(AllowEmptyStrings = false, ErrorMessage = "A stationery code is required.")]
        public string Code { get; set; }
        [Display(Name = "Description")]
        [Required(AllowEmptyStrings = false, ErrorMessage = "Column description can't be blank.")]
        public string Description { get; set; }
        [Display(Name = "Category")]
        [Required(AllowEmptyStrings = false, ErrorMessage = "Column category can't be blank.")]
        public string Category { get; set; }
        [Display(Name = "Price")]
        [Required(AllowEmptyStrings = false, ErrorMessage = "Column price can't be blank.")]
        public float Price { get; set; }
        [Display(Name = "Qty")]
        [Required(AllowEmptyStrings = false, ErrorMessage = "Column Qty can't be blank.")]
        public int Qty { get; set; }
        [Display(Name = "Reorder Level")]
        [Required(AllowEmptyStrings = false, ErrorMessage = "Column ReorderLevel can't be blank.")]
        public int ReorderLevel { get; set; }
        [Display(Name = "Reorder Qty")]
        [Required(AllowEmptyStrings = false, ErrorMessage = "Column ReorderQty can't be blank.")]
        public int ReorderQty { get; set; }
        [Display(Name = "UOM")]
        [Required(AllowEmptyStrings = false, ErrorMessage = "Column UOM can't be blank.")]
        public string UOM { get; set; }
    }
}